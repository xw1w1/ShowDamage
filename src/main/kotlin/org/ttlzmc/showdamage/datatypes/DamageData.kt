package org.ttlzmc.showdamage.datatypes

import org.bukkit.Location
import org.bukkit.damage.DamageSource
import org.bukkit.entity.Entity

data class DamageData (
    private val source: DamageSource,
    private val reciever: Entity,
    private val damageDealt: Double,
    private val location: Location,
    ){
    private val valid: Boolean = BukkitDamageTypeMask.isValidDamageType(source.damageType)

    fun isValid(): Boolean {
        return this.valid;
    }


}
