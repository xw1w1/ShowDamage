package org.ttlzmc.showdamage.datatypes

import org.bukkit.Location
import org.bukkit.damage.DamageSource
import org.bukkit.entity.Entity

@Suppress("UnstableApiUsage")
data class DamageData (
    private val source: DamageSource,
    private val damageType: DamageType,
    private val damageDealt: Double,
    private val damagedEntities: List<Entity>
){
    private val valid: Boolean = BukkitDamageMask.isValidDamageType(source.damageType)
    private val location = pushLocation(completeLocation(), y = 5.0)

    fun isValid(): Boolean {
        return this.valid
    }

    private fun pushLocation(location: Location, x: Double = 0.0, y: Double = 0.0, z: Double = 0.0) {
        location.add(x, y, z)
    }

    private fun completeLocation(): Location {
        check(damagedEntities.isEmpty()) { "Cannot calculate center for empty entity list. How did you even do that?" }
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
