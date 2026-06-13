package dev.craftwarestudios.showdamage

/**
 * Represents a specific type of attack that caused damage.
 *
 * This class is used to describe exactly what happened inside the [org.bukkit.event.entity.EntityDamageByEntityEvent].
 *
 * Unlike [AttackGroup], which defines how attacks are aggregated and processed internally, this class
 * describes the actual gameplay action, that occurred.
 *
 * A complete attack description is represented by [dev.craftwarestudios.showdamage.dao.AttackClassification], which combines both [AttackKind] and [AttackGroup].
 *
 * @author xw1w1
 * @since 2.0
 */
enum class AttackKind(val defaultBadge: String?)
{
    MELEE(null),
    SWORD_SWEEP(null),
    AXE(null),
    SPEAR(null),
    TRIDENT(null),
    PROJECTILE(null),
    MACE_SMASH("⟡"),
    EXPLOSION("✹");
}
