package org.ttlzmc.showdamage

import org.bukkit.event.HandlerList
import org.bukkit.plugin.java.JavaPlugin
import org.ttlzmc.showdamage.api.ShowDamageAPI
import org.ttlzmc.showdamage.api.ShowDamageAPIProvider

class ShowDamage : JavaPlugin() {

    override fun onEnable() {
        val startMs = System.currentTimeMillis()
        ShowDamageConfiguration.loadConfiguration()
        this.slF4JLogger.info("ShowDamage enabled!")
        val endMs = System.currentTimeMillis()
        this.slF4JLogger.info("Configuration loaded in ${endMs - startMs}ms")

        ShowDamageAPI.register(ShowDamageAPIProvider)
        server.pluginManager.registerEvents(DamageEventHandler, this)
    }

    override fun onDisable() {
        this.slF4JLogger.info("Look what you've done!")
        ShowDamageConfiguration.saveConfiguration()
        HandlerList.unregisterAll(DamageEventHandler)
    }

    companion object {
        fun getInstance() = getPlugin(ShowDamage::class.java)
    }
}