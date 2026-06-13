package dev.craftwarestudios.showdamage.configuration

import com.google.gson.*
import org.bukkit.configuration.file.FileConfiguration
import java.io.File
import java.io.InputStream

internal class ConfigurationFile(private val src: File) : FileConfiguration() {
    private val gson: Gson = GsonBuilder()
        .setPrettyPrinting()
        .create()

    private var configurationContents: JsonObject = JsonObject()

    fun loadFromFile(file: File) {
        if (!file.exists() || !file.isFile || !file.canRead()) {
            throw IllegalStateException("Configuration is not readable.")
        }

        try {
            var input = file.readText(Charsets.UTF_8)
            if (input.isBlank() || input.isEmpty()) input = "{}"

            this.configurationContents = JsonParser.parseString(input).asJsonObject
        } catch (any: Exception) {
            if ((any is JsonParseException) or (any is JsonSyntaxException)) {
                throw IllegalArgumentException("JSON parsing error, please check configuration syntax: ", any)
            }

            throw RuntimeException("Error while parsing configuration: ", any)
        }
    }

    fun loadFromBytes(bytes: InputStream) {
        if (bytes.available() <= 0) throw IllegalArgumentException("Empty configuration file.")

        Gson().newJsonReader(bytes.reader()).use {
            this.configurationContents = gson.fromJson(it, JsonObject::class.java)
        }
    }

    override fun loadFromString(contents: String) {
        val content = if (contents.isBlank() || contents.isEmpty()) "{}" else contents
        this.configurationContents = JsonParser.parseString(content).asJsonObject
        this.src.writeText(this.saveToString())
    }

    override fun saveToString(): String {
        return this.gson.toJson(this.configurationContents)
    }

    override fun get(path: String, def: Any?): Any? {
        var pathPart = this.configurationContents
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
        var pathPart = this.configurationContents
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
            null -> JsonNull.INSTANCE
            is Number -> JsonPrimitive(this)
            is String -> JsonPrimitive(this)
            is Boolean -> JsonPrimitive(this)
            is JsonElement -> this
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