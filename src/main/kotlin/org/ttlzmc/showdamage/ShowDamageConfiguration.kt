package org.ttlzmc.showdamage

import org.ttlzmc.showdamage.util.JsonConfiguration
import java.io.File

object ShowDamageConfiguration {
    private lateinit var json: JsonConfiguration

    fun loadConfiguration(): ShowDamageConfiguration {
        this.json = JsonConfiguration(File(ShowDamage.getInstance().dataFolder, "config.json"))
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