package dev.craftwarestudios.showdamage

import dev.craftwarestudios.showdamage.configuration.ConfigurationFile
import java.io.File

class ShowDamageConfiguration {
    private lateinit var configuration: ConfigurationFile
    private val configurationName: String = "configuration.json"

    fun reloadConfiguration() {
        val file = File(ShowDamage.instance.dataFolder, this.configurationName)
        this.configuration = ConfigurationFile(file)

        if (file.exists()) {
            this.configuration.loadFromFile(file)
        } else {
            val defaultConfig = this.javaClass.getResourceAsStream("/$configurationName")
            if (defaultConfig == null) {
                throw IllegalArgumentException("Default resource configuration.json is not present")
            }

            configuration.loadFromBytes(defaultConfig)
            this.saveConfiguration()
        }
    }

    fun saveConfiguration() {
        this.configuration.save(File(ShowDamage.instance.dataFolder, this.configurationName))
    }

    /**
     * Get any Boolean from the JSON Object by path.
     * path is delimited by '.'
     * @param path
     * @return a boolean
     */
    fun getBoolean(path: String, def: Boolean = false): Boolean {
        return this.configuration.getBoolean(path, def)
    }

    /**
     * Get any String from the JSON Object by path.
     * path is delimited by '.'
     * @param path
     * @return a string
     */
    fun getString(path: String, def: String = ""): String {
        return this.configuration.getString(path, def) ?: def
    }


    /**
     * Get any Double from the JSON Object by path.
     * path is delimited by '.'
     * @param path
     * @return a double
     */
    fun getDouble(path: String, def: Double = 0.0): Double {
        return this.configuration.getDouble(path, def)
    }

    /**
     * Get any Int from the JSON Object by path.
     * path is delimited by '.'
     * @param path
     * @return an int
     */
    fun getInt(path: String, def: Int = 0): Int {
        return this.configuration.getInt(path, def)
    }
}