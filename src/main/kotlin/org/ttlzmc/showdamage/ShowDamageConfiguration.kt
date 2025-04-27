package org.ttlzmc.showdamage

import org.ttlzmc.showdamage.util.JsonConfiguration
import java.io.File

object ShowDamageConfiguration {
    private lateinit var json: JsonConfiguration

    fun loadConfiguration(): ShowDamageConfiguration {
        // What happens if this file is not present?
        val config = File(ShowDamage.getInstance().dataFolder, "config.json")
        this.json = JsonConfiguration(config) // No loading yet, we need this.json.file to be == config

        if (config.exists()) {
            this.json.loadFromFile(config)
            return this
        } // END: Early exit

        // Note: /config.json requires to be prefixed with '/'
        val defaultConfigUrl = ShowDamageConfiguration.javaClass.getResourceAsStream("/config.json")

        if (defaultConfigUrl == null) {
            throw IllegalArgumentException("No resource config.json present.")
        }

        /* Loading resources by file from a JAR is impossible. The OS sees the JAR as one file.
        *  You can't load a file from within a file. The resource itself is perceived as a stream of bytes
        *  on the JAR with some length of the bytes. We let GSON load this for us. */
        this.json.loadFromBytes(defaultConfigUrl)

        return this
    }

    fun saveConfiguration() {
        this.json.save(File(ShowDamage.getInstance().dataFolder, "config.json"))
    }

    /**
     * Get any Boolean from the JSON Object by path.
     * path is delimited by '.'
     * @param path
     * @return a boolean
     */
    fun getBoolean(path: String): Boolean {
        return this.json.getBoolean(path)
    }

    /**
     * Get any String from the JSON Object by path.
     * path is delimited by '.'
     * @param path
     * @return a string
     */
    fun getString(path: String): String {
        return this.json.getString(path) ?: ""
    }


    /**
     * Get any Double from the JSON Object by path.
     * path is delimited by '.'
     * @param path
     * @return a double
     */
    fun getDouble(path: String): Double {
        return this.json.getDouble(path)
    }

    /**
     * Get any Int from the JSON Object by path.
     * path is delimited by '.'
     * @param path
     * @return an int
     */
    fun getInt(path: String): Int {
        return this.json.getInt(path)
    }
    fun getJSON() : JsonConfiguration{
        return this.json;
    }
}