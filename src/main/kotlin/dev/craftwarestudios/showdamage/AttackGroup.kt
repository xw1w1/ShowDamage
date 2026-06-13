package dev.craftwarestudios.showdamage

/**
 * Category of an attack.
 *
 * Attack groups are used by [DamageClassifier] to determine how individual damage events
 * should be merged and processed.
 *
 * This enum, unlike [AttackKind], describes the behavioural family that the specific attack belongs to.
 * A complete attack description is represented by [dev.craftwarestudios.showdamage.dao.AttackClassification], which combines both [AttackGroup] and [AttackKind].
 *
 * @author xw1w1
 * @since 2.0
 */
enum class AttackGroup
{
    DIRECT,
    SWING,
    EXPLOSION,
    MACE,
    PROJECTILE
}
