package dev.craftwarestudios.showdamage.damage

import dev.craftwarestudios.showdamage.dao.AttackClassification
import dev.craftwarestudios.showdamage.AttackKind
import dev.craftwarestudios.showdamage.ShowDamage
import dev.craftwarestudios.showdamage.dao.DamageAmount
import org.bukkit.Location
import org.bukkit.entity.Entity
import java.util.UUID

/**
 * This class represents a collection of damage events that were merged
 * into a single logical attack.
 *
 * Cluster accumulates damage, affected targets count and some attack metadata
 * until it is finalized and displayed to players.
 *
 * Depending on the associated [MultipleDamageMergeStrategy], multiple damage
 * events may be merged into a single [DamageCluster].
 *
 * Instances of this class should only be managed internally and
 * are considered mutable while aggregation is in progress.
 *
 * @author xw1w1
 * @since 2.0
 */
data class DamageCluster(
    val id: Int,
    var anchor: Location,
    val sourceId: UUID?,
    val damager: Entity,

    val mergeStrategy: MultipleDamageMergeStrategy,
    val classification: AttackClassification,

    var damage: DamageAmount,

    var splashDamage: DamageAmount? = null,
    var splashTargets: Int = 0,

    var critical: Boolean,
    var size: Int = 1,

    var sawSweepHit: Boolean = splashTargets > 0
) {
    val displayKind: AttackKind = when (mergeStrategy) {
        MultipleDamageMergeStrategy.EXPLOSION -> AttackKind.EXPLOSION
        MultipleDamageMergeStrategy.MACE -> AttackKind.MACE_SMASH
        MultipleDamageMergeStrategy.SWING -> {
            if (sawSweepHit || classification.kind == AttackKind.SWORD_SWEEP) AttackKind.SWORD_SWEEP else classification.kind
        }
    }

    fun canMergeWith(location: Location, sourceId: UUID?, strategy: MultipleDamageMergeStrategy, mergeRadiusSquared: Double): Boolean {
        val world = anchor.world ?: return false
        val otherWorld = location.world ?: return false
        if (world.uid != otherWorld.uid) return false
        if (this.sourceId != sourceId) return false
        if (this.mergeStrategy != strategy) return false

        return when (strategy) {
            MultipleDamageMergeStrategy.EXPLOSION -> true
            MultipleDamageMergeStrategy.SWING,
            MultipleDamageMergeStrategy.MACE -> anchor.distanceSquared(location) <= mergeRadiusSquared
        }
    }

    fun absorb(hitLocation: Location, hitDamage: DamageAmount, isCritical: Boolean, isSplash: Boolean) {
        val previousSize = this.size
        this.size += 1
        this.critical = this.critical || isCritical
        this.anchor = blendAnchor(this.anchor, hitLocation, previousSize, this.size)

        if (isSplash) {
            splashDamage = if (splashDamage == null) hitDamage else splashDamage!!.plus(hitDamage)
            this.splashTargets += 1
        } else {
            damage = damage.plus(hitDamage)
        }
    }

    private fun blendAnchor(current: Location, incoming: Location, previousSize: Int, newSize: Int): Location {
        val world = current.world ?: return incoming.clone()
        if (incoming.world?.uid != world.uid) return current

        val factorA = previousSize.toDouble()
        val factorB = 1.0
        val total = newSize.toDouble()

        return current.clone().apply {
            x = ((current.x * factorA) + incoming.x * factorB) / total
            y = ((current.y * factorA) + incoming.y * factorB) / total
            z = ((current.z * factorA) + incoming.z * factorB) / total
            yaw = incoming.yaw
            pitch = incoming.pitch
        }
    }

    companion object {
         fun createPopupLocation(entity: Entity): Location {
            val config = ShowDamage.instance.configuration
            val loc = entity.location.clone()
            loc.y += entity.boundingBox.height + config.getDouble("display-settings.popup-additional-height", 0.15)
            return loc
        }
    }
}
