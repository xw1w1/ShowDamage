package org.ttlzmc.showdamage

import org.bukkit.plugin.java.JavaPlugin

class ShowDamage : JavaPlugin() {

    override fun onEnable() {
        val startMs = System.currentTimeMillis()
        ShowDamageConfiguration.loadConfiguration()
        this.slF4JLogger.info("ShowDamage enabled!")
        val endMs = System.currentTimeMillis()
        this.slF4JLogger.info("Configuration loaded in ${endMs - startMs}ms")
    }

    override fun onDisable() {
        this.slF4JLogger.info("Look what you've done!")
        ShowDamageConfiguration.saveConfiguration()
    }

    companion object {
        fun getInstance() = getPlugin(ShowDamage::class.java)
        fun getConfig() = ShowDamageConfiguration
    }
}