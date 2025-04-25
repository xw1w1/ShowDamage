package org.ttlzmc.showdamage

import net.kyori.adventure.text.Component
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.entity.Display
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.entity.TextDisplay
import org.ttlzmc.showdamage.datatypes.DisplaySettings

class DamageDisplay(
    private val settings: DisplaySettings,
    private val damageDealer: Entity
) {
    private val textEntity: TextDisplay

    // don't touch this, i will fix it in the next commit
    init {
        textEntity = computedLocation.world.spawn(computedLocation, TextDisplay::class.java).apply {
            text(Component.empty())
            isInvulnerable = true
            isPersistent = false
            setGravity(false)

            isSeeThrough = config.getBoolean("visibility.show-through")
            backgroundColor = Color.fromARGB(
                config.getInt("colors.popup-background-transparency"),
                0,
                0,
                0
            )
            billboard = Display.Billboard.CENTER

            if (config.getBoolean("visibility.visible-to-everyone")) {
                isVisibleByDefault = true
            } else {
                isVisibleByDefault = false
                showToDamagerAndNearbyPlayers(damager, location)
            }
        }

        val instance = ShowDamage.getInstance()
        instance.server.scheduler.runTaskLater(instance, Runnable {
            textEntity.remove()
        }, (config.getDouble("visibility.popup-lifetime") * 20).toLong())
    }

    fun remove() {
        textEntity.remove()
    }

    private fun showToDamagerAndNearbyPlayers(damager: Entity?, location: Location) {
        val config = ShowDamage.getConfig()
        val players = location.getNearbyPlayers(
            config.getDouble("visibility.visibility-distance"),
            config.getDouble("visibility.visibility-distance"),
            config.getDouble("visibility.visibility-distance")
        )

        if (damager is Player && damager.isOnline) {
            damager.showEntity(ShowDamage.getInstance(), textEntity)
        }

        players.filter { it != damager }.forEach {
            it.showEntity(ShowDamage.getInstance(), textEntity)
        }
    }
}