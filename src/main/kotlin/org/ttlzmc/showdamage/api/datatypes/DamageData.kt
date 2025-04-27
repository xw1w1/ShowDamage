package org.ttlzmc.showdamage.api.datatypes

import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.event.entity.EntityDamageEvent

@Suppress("UnstableApiUsage")
data class DamageData (
    private val source: Entity,
    private val damageType: EntityDamageEvent.DamageCause,
    private val damageDealt: Double,
    private val damageRecord: DamageRecord
){
    private val valid: Boolean = true //FIXME:BukkitDamageMask.isValidDamageType(source.damageType)
    private val location = pushLocation(completeLocation(), y = 2.2)

    fun isValid(): Boolean {
        return this.valid
    }

    fun getDamageDealt(): Double = damageDealt

    fun getLocation() = this.location

    private fun pushLocation(location: Location, x: Double = 0.0, y: Double = 0.0, z: Double = 0.0) = location.add(x, y, z)

    private fun completeLocation(): Location {
        val damagedEntities = damageRecord.getTargets()
        check(!damagedEntities.isEmpty()) { "Cannot calculate center for empty entity list. How did you even do that?" }
        if (damagedEntities.size == 1) return damagedEntities.first().location
        val world = damagedEntities.first().location.world
        val (totalX, totalY, totalZ) = damagedEntities.fold(Triple(0.0, 0.0, 0.0)) { (accX, accY, accZ), entity ->
            val loc = entity.location
            Triple(accX + loc.x, accY + loc.y, accZ + loc.z)
        }
        val size = damagedEntities.size
        return Location(world, totalX / size, totalY / size, totalZ / size)
    }
}
