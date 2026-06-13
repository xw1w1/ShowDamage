package dev.craftwarestudios.showdamage.dao

import dev.craftwarestudios.showdamage.AttackGroup
import dev.craftwarestudios.showdamage.AttackKind

/**
 * Fully describes a damage event.
 *
 * Parts of this class are used to define what and how it happened in the event.
 *
 * @author xw1w1
 * @since 2.0
 */
data class AttackClassification(val kind: AttackKind, val group: AttackGroup)