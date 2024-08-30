@file:JvmName("KotlinExtensions")
package ru.xw1w1.lookwhatyoudone.util

import org.bukkit.Material
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack

fun ItemStack.isSword(): Boolean {
    val swords = arrayOf(
        Material.WOODEN_SWORD,
        Material.STONE_SWORD,
        Material.IRON_SWORD,
        Material.GOLDEN_SWORD,
        Material.DIAMOND_SWORD,
        Material.NETHERITE_SWORD
    )

    return swords.contains(this.type)
}

fun ItemStack.isMace(): Boolean {
    return this.type == Material.MACE
}

fun Entity.isProjectile(): Boolean {
    val projectiles = arrayOf(
        EntityType.ARROW,
        EntityType.SPECTRAL_ARROW,
        EntityType.EGG,
        EntityType.SNOWBALL,
        EntityType.TRIDENT
    )

    return projectiles.contains(this.type)
}
