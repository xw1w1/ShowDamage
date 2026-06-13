package dev.craftwarestudios.showdamage.dao

/**
 * Represents the result of a [dev.craftwarestudios.showdamage.damage.DamageCluster] registration attempt.
 *
 * Contains the [id] of the target cluster and indicates
 * whether a new cluster was [created] or an existing one was reused.
 *
 * Used internally by the damage aggregation system.
 *
 * @author xw1w1
 * @since 2.0
 */
data class DamageClusterTicket(val id: Int, val created: Boolean)