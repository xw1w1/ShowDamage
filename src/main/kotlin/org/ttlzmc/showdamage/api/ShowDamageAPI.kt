package org.ttlzmc.showdamage.api


import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.annotations.ApiStatus
import org.ttlzmc.showdamage.DisplayRemoveRunnable
import org.ttlzmc.showdamage.ShowDamage
import org.ttlzmc.showdamage.ShowDamageConfiguration
import org.ttlzmc.showdamage.api.datatypes.DamageData
import org.ttlzmc.showdamage.api.datatypes.DamageType
import org.ttlzmc.showdamage.api.datatypes.DisplaySettings
import org.ttlzmc.showdamage.util.JsonConfiguration
import java.util.Objects.isNull

interface ShowDamageAPI {
    @ApiStatus.Internal
    fun defaultProvider() = ShowDamageAPIProvider

    fun createDisplay(data: DamageData, settings: DisplaySettings): DamageDisplay

    fun getPlugin(): JavaPlugin

    fun delegateRemoveExec(display: DamageDisplay) {
        val timeout = ShowDamageConfiguration.getJSON().getDouble("visibility.popup-lifetime").toLong()
        val tickRate = ShowDamage.getInstance().server.serverTickManager.tickRate.toLong()

        // Schedules this to execute synchronously.
        DisplayRemoveRunnable(display).runTaskLater(getPlugin(), timeout*tickRate)
    }
    fun parseSettings(json: JsonConfiguration, type: DamageType, critical: Boolean): DisplaySettings {
        val isSeeThrough = json.getBoolean("visibility.show-through")
        val visibleToEveryone = json.getBoolean("visibility.visible-to-everyone")
        val visibilityRadius = json.getDouble("visibility.visibility-distance")
        val backgroundTransparency = json.getInt("colors.popup-background-transparency")
        val popupLifetime = json.getDouble("visibility.popup-lifetime")
        return DisplaySettings(isSeeThrough, visibleToEveryone, visibilityRadius, backgroundTransparency, popupLifetime, type, critical)
    }

    companion object {
        private var INSTANCE: ShowDamageAPI? = null

        fun get(): ShowDamageAPI {
            val instance: ShowDamageAPI? = INSTANCE
            if (isNull(INSTANCE)) throw NullPointerException("ShowDamageAPI is not initialized.")
            return instance!!
        }

        @ApiStatus.NonExtendable
        fun register(instance: ShowDamageAPI) {
            if (isNull(INSTANCE)) INSTANCE = instance
            else throw RuntimeException("ShowDamageAPI instance is already set. You are trying to set API instance twice?")
        }
    }
}