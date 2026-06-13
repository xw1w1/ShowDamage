package dev.craftwarestudios.showdamage.damage

import dev.craftwarestudios.showdamage.dao.AttackClassification
import dev.craftwarestudios.showdamage.AttackGroup

/**
 * This class defines how multiple damage events should be grouped into
 * a single [DamageCluster].
 *
 * Different attack types may require different aggregation rules.
 *
 * The selected strategy determines whether the incoming damage events
 * are considered as a part of the same attack.
 *
 * @author xw1w1
 * @since 2.0
 *
 * @see AttackClassification
 */
enum class MultipleDamageMergeStrategy
{
    SWING,
    EXPLOSION,
    MACE
    ;

    companion object {
        /**
         * Resolves the merge strategy associated with the supplied [AttackClassification].
         *
         * Returns `null` when the attack type does not support multi-hit aggregation.
         */
        fun from(classification: AttackClassification): MultipleDamageMergeStrategy? {
            return when (classification.group) {
                AttackGroup.SWING, AttackGroup.DIRECT -> SWING
                AttackGroup.EXPLOSION -> EXPLOSION
                AttackGroup.MACE -> MACE
                else -> null
            }
        }
    }
}
