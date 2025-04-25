package org.ttlzmc.showdamage.util

import com.google.gson.*
import org.bukkit.configuration.file.FileConfiguration
import java.io.File

class JsonConfiguration(private var file: File) : FileConfiguration() {

    private val gson = Gson()
    private var json: JsonObject = JsonObject()

    init {
        if (!file.exists() || !file.isFile || !file.canRead()) {
            throw IllegalStateException("Configuration is not loaded or not readable")
        }

        try {
            var rawJson = this.file.readText()
            if (rawJson.isBlank()) rawJson = "{}"

            this.json = JsonParser.parseString(rawJson).asJsonObject
        } catch (ex: Exception) {
            if ((ex is JsonParseException) or (ex is JsonSyntaxException)) {
                throw IllegalArgumentException("Json parsing error! Check configuration syntax.")
            }
        }
    }

    override fun saveToString(): String {
        return gson.toJson(this.json)
    }

    override fun loadFromString(contents: String) {
        var content = contents
        if (content.isBlank() or content.isEmpty()) content = "{}"
        json = JsonParser.parseString(content).asJsonObject
        file.writeText(gson.toJson(json))
    }

    override fun get(path: String, def: Any?): Any? {
        var pathPart = json
        val pathParts = path.split(".")
        for (i in pathParts.indices) {
            val part = pathParts[i]
            if (i == pathParts.indices.last) {
                return pathPart[part]?.getValue() ?: def
            } else {
                pathPart = (pathPart[part] ?: return def).asJsonObject
            }
        }

        return null
    }

    override fun set(path: String, value: Any?) {
        var pathPart = json
        val pathParts = path.split(".")
        for (i in pathParts.indices) {
            val part = pathParts[i]
            if (i == pathParts.indices.last) {
                pathPart.add(part, value.toJsonElement())
            } else {
                val obj = pathPart[part]?.asJsonObject
                val newObject = obj ?: JsonObject()
                if (obj == null) pathPart.add(part, newObject)
                pathPart = newObject
            }
        }
    }

    private fun Any?.toJsonElement(): JsonElement {
        return when (this) {
            is Number -> JsonPrimitive(this)
            is String -> JsonPrimitive(this)
            is Char -> JsonPrimitive(this)
            is Boolean -> JsonPrimitive(this)
            is JsonElement -> this
            null -> JsonNull.INSTANCE
            else -> JsonPrimitive(this.toString())
        }
    }

    private fun JsonElement.getValue(): Any? {
        return when (this) {
            is JsonPrimitive -> when {
                this.isString -> this.asString
                this.isNumber -> this.asNumber
                this.isBoolean -> this.asBoolean
                else -> null
            }
            is JsonArray -> this.map { it.getValue() }.toTypedArray()
            is JsonObject -> this.entrySet().associate { it.key to it.value.getValue() }
            is JsonNull -> null
            else -> null
        }
    }
}