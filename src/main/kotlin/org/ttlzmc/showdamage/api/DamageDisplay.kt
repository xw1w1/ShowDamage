package org.ttlzmc.showdamage.api

import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.entity.TextDisplay
import org.ttlzmc.showdamage.ShowDamage

class DamageDisplay(
    private val showDistance: Double,
    private val textEntity: TextDisplay
) {
    private fun show(damageDealer: Entity, location: Location) {
        val players = location.getNearbyPlayers(showDistance, showDistance, showDistance)

        if (damageDealer is Player && damageDealer.isOnline) {
            damageDealer.showEntity(ShowDamage.getInstance(), textEntity)
        }

        players.filter { it != damageDealer }.forEach {
            it.showEntity(ShowDamage.getInstance(), textEntity)
        }
    }

    fun remove() = textEntity.remove()
}