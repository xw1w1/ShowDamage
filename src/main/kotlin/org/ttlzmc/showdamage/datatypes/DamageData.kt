package org.ttlzmc.showdamage.datatypes

import org.bukkit.Location
import org.bukkit.damage.DamageSource
import org.bukkit.entity.Entity

data class DamageData (
    private val source: DamageSource,
    private val reciever: Entity,
    private val damageDealt: Double,
    ){
    private val valid: Boolean = BukkitDamageMask.isValidDamageType(source.damageType)
    private val location = pushLocation(reciever.location, y = 5.0)

    fun isValid(): Boolean {
        return this.valid;
    }

    private fun pushLocation(location: Location, x: Double = 0.0, y: Double = 0.0, z: Double = 0.0) {
        location.add(x, y, z)
    }


}
