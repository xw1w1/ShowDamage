package org.ttlzmc.showdamage

import com.google.gson.JsonObject
import org.ttlzmc.showdamage.util.JsonConfiguration
import java.io.File
import kotlin.io.path.toPath

object ShowDamageConfiguration {
    private lateinit var json: JsonConfiguration

    fun loadConfiguration(): ShowDamageConfiguration {
        // What happens if this file is not present?
        val config = File(ShowDamage.getInstance().dataFolder, "config.json")
        this.json = JsonConfiguration(config) // No loading yet

        if (config.exists()) {
            this.json.loadFromFile(config)
            return this
        } // END: Early exit

        // Note: /config.json requires to be prefixed with '/'
        val defaultConfigUrl = ShowDamageConfiguration.javaClass.getResourceAsStream("/config.json")

        if (defaultConfigUrl == null) {
            throw IllegalArgumentException("No resource config.json present.")
        }

        /* Loading resources from a JAR is impossible. The OS sees the JAR as one file.
        *  You can't load a file from within a file. The resource itself is perceived as a stream of bytes
        *  on the JAR with some length of the bytes. We let GSON load this for us. */
        this.json.loadFromBytes(defaultConfigUrl)

        return this
    }

    fun saveConfiguration() {
        this.json.save(File(ShowDamage.getInstance().dataFolder, "config.json"))
    }

    fun getBoolean(path: String): Boolean {
        return this.json.getBoolean(path)
    }

    fun getString(path: String): String {
        return this.json.getString(path) ?: ""
    }

    fun getDouble(path: String): Double {
        return this.json.getDouble(path)
    }

    fun getInt(path: String): Int {
        return this.json.getInt(path)
    }
}