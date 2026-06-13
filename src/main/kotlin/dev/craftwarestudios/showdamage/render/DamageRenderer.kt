package dev.craftwarestudios.showdamage.render

import dev.craftwarestudios.showdamage.AttackKind
import dev.craftwarestudios.showdamage.ShowDamage
import dev.craftwarestudios.showdamage.dao.DamageAmount
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

object DamageRenderer {
    fun showDamage(
        location: Location,
        primaryDamage: DamageAmount, splashDamage: DamageAmount? = null,
        isCritical: Boolean, kind: AttackKind,
        clusterSize: Int, damager: Entity
    ) {
        val plugin = ShowDamage.instance
        val controller = DamageDisplay(location, DamageFormatter.createClusterComponentMultiline(
            primaryDamage, splashDamage, clusterSize, kind, isCritical
        )).also { it.configureVisibility(damager) }

        val lifetimeTicks = plugin.configuration.getInt("display-settings.popup-lifetime-ticks", 25).toLong().coerceAtLeast(1L)
        plugin.server.scheduler.runTaskLater(plugin, Runnable {
            controller.remove()
        }, lifetimeTicks)
    }

    fun showDamageInline(
        location: Location,
        primaryDamage: DamageAmount, splashDamage: DamageAmount? = null,
        isCritical: Boolean, kind: AttackKind,
        clusterSize: Int, damager: Entity
    ) {
        val plugin = ShowDamage.instance
        val controller = DamageDisplay(location, DamageFormatter.createClusterComponentInlined(
            primaryDamage, splashDamage, clusterSize, kind, isCritical
        )).also { it.configureVisibility(damager) }

        val lifetimeTicks = plugin.configuration.getInt("display-settings.popup-lifetime-ticks", 25).toLong().coerceAtLeast(1L)
        plugin.server.scheduler.runTaskLater(plugin, Runnable {
            controller.remove()
        }, lifetimeTicks)
    }

    fun showCompensation(target: Player, damage: DamageAmount) {
        DamageFormatter.createCompensatedDamageComponent(damage)?.apply(target::sendActionBar)
    }
}