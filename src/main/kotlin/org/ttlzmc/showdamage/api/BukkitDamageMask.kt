package org.ttlzmc.showdamage.api

import org.bukkit.damage.DamageType

object BukkitDamageMask {
    private val damageTypeMask = setOf( "wind_charge", "out_of_world", "outside_border", "thrown", "starve", "drown", "dry_out", "fireworks",)

    fun isValidDamageType(damageType: DamageType): Boolean {
        // Could improve performance here
       return damageType.key.value() !in damageTypeMask;
    }


}