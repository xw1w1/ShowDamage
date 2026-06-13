package dev.craftwarestudios.showdamage.render

import dev.craftwarestudios.showdamage.ShowDamage
import dev.craftwarestudios.showdamage.command.ShowDamageCommand
import net.kyori.adventure.text.Component
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.entity.Display
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.entity.TextDisplay
import org.bukkit.persistence.PersistentDataType
import java.util.*

class DamageDisplay
    (private val location: Location, private val popup: Component)
{
    private val entity: TextDisplay = spawn()

    private fun spawn(): TextDisplay {
        val config = ShowDamage.instance.configuration
        val world = this.location.world ?: error("Popup location has no world")

        return world.spawn(this.location, TextDisplay::class.java).apply {
            text(popup)
            billboard = Display.Billboard.CENTER
            isSeeThrough = config.getBoolean("display-settings.show-through-walls", true)
            backgroundColor = Color.fromARGB(
                config.getInt("display-settings.popup-background-transparency", 64),
                0,
                0,
                0
            )
        }
    }

    fun configureVisibility(damager: Entity) {
        this.entity.isInvulnerable = true
        this.entity.setGravity(false)

        val config = ShowDamage.instance.configuration
        if (damager is Player && config.getBoolean("display-settings.show-to-damage-source-only", false)) {
            this.entity.isVisibleByDefault = false
            damager.showEntity(ShowDamage.instance, this.entity)
        }

        val world = this.location.world

        if (world != null) {
            val radius = config.getDouble("display-settings.popup-visibility-distance", 32.0)
            val radiusSquared = radius * radius

            for (player in world.players) {
                val playerLocation = player.location
                if (playerLocation.world?.uid != world.uid) continue
                if (playerLocation.distanceSquared(this.location) > radiusSquared) continue
                if (config.getBoolean("display-settings.allow-toggle-command", true) && !isVisibleForPlayer(player)) {
                    player.hideEntity(ShowDamage.instance, entity)
                }
            }
        }
    }

    fun remove() {
        this.entity.remove()
    }

    private fun isVisibleForPlayer(player: Player): Boolean {
        val key = ShowDamageCommand.PDC_VISIBILITY_KEY
        val container = player.persistentDataContainer

        if (!container.has(key)) return true
        return Objects.requireNonNull(container.get(key, PersistentDataType.INTEGER))!! > 0
    }
}
