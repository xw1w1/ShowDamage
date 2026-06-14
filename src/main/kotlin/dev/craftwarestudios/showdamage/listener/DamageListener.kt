package dev.craftwarestudios.showdamage.listener

import dev.craftwarestudios.showdamage.AttackGroup
import dev.craftwarestudios.showdamage.DamageClassifier
import dev.craftwarestudios.showdamage.ShowDamage
import dev.craftwarestudios.showdamage.dao.DamageAmount
import dev.craftwarestudios.showdamage.damage.DamageCluster
import dev.craftwarestudios.showdamage.dao.AttackClassification
import dev.craftwarestudios.showdamage.render.DamageFormatter
import dev.craftwarestudios.showdamage.render.DamageRenderer
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

class DamageListener : Listener {
    @EventHandler(ignoreCancelled = true)
    fun onDamage(event: EntityDamageByEntityEvent) {
        val config = ShowDamage.instance.configuration
        val filtering = config.getBoolean("display-settings.filter-non-living-entities", true)
        if (shouldIgnore(event.entity, filtering)) return

        val damage = DamageAmount(event.finalDamage, event.damage)
        val classification = DamageClassifier.classify(event)

        val mergeRadius = config.getDouble("display-settings.multiple-entities-count-distance", 10.0)
        val mergeDelayTicks = config.getInt("display-settings.multiple-entities-merge-timeout-ticks", 2).toLong()

        val ticket = if (config.getBoolean("display-settings.count-multiple-entities")) {
            ShowDamage.instance.clusterCache.register(
                event = event,
                classification = classification,
                damage = damage,
                critical = event.isCritical,
                mergeRadius = mergeRadius
            )
        } else null

        if (event.entity is Player) {
            handleTargetHotbarMessage(event.entity as Player, damage)
        }

        if (classification.group == AttackGroup.PROJECTILE) {
            handleProjectileMessages(event, damage, classification)
        }

        if (ticket != null) {
            if (ticket.created) {
                ShowDamage.instance.clusterCache.scheduleFinalization(ticket.id, mergeDelayTicks) { cluster ->
                    DamageRenderer.showDamage(
                        cluster.anchor,
                        cluster.damage,
                        cluster.splashDamage,
                        cluster.critical,
                        cluster.displayKind,
                        cluster.splashTargets,
                        cluster.damager
                    )
                }
            }
            return
        }

        DamageRenderer.showDamage(
            DamageCluster.createPopupLocation(event.entity),
            damage,
            null,
            event.isCritical,
            classification.kind,
            1,
            event.damager
        )
    }

    /**
     * Shows a special chat message to the attacker if the squared distance between
     * the attacker and attacked target is greater than `"display-settings.projectile-message-distance"`.
     *
     * Also shows a dealt damage to the attacker through the message in a hotbar.
     */
    private fun handleProjectileMessages(event: EntityDamageByEntityEvent, damage: DamageAmount, classification: AttackClassification) {
        val config = ShowDamage.instance.configuration
        val shooter = event.damageSource.causingEntity as? Player ?: return
        val threshold = config.getDouble("display-settings.projectile-message-distance", 50.0)
        if (shooter.location.distanceSquared(event.entity.location) > threshold) {
            if (config.getBoolean("messages.chat-messages", true)) {
                shooter.sendMessage(DamageFormatter.createProjectileComponentMessage(event.entity.name, damage))
            }

            if (config.getBoolean("messages.hotbar-messages", true)) {
                shooter.sendActionBar(DamageFormatter.createClusterComponentInlined(
                    damage, null, 0, classification.kind, event.isCritical
                ))
            }
        }
    }

    /**
     * Shows a negated damage to the [target].
     */
    private fun handleTargetHotbarMessage(target: Player, damage: DamageAmount) {
        val config = ShowDamage.instance.configuration
        if (!config.getBoolean("messages.hotbar-messages", true)) return

        DamageFormatter.createCompensatedDamageComponent(damage)?.apply(target::sendActionBar)
    }

    private fun shouldIgnore(entity: Entity, ignoreNonLivingEntities: Boolean): Boolean {
        if (entity.type == EntityType.ITEM) return true
        // Probably for removal, in past this thing was used to ignore legacy Damage Displays
        if (entity.type == EntityType.ARMOR_STAND && entity.isInvulnerable) return true

        // from configuration:filter-non-living-entities
        if (entity !is LivingEntity && ignoreNonLivingEntities) return true
        return false
    }
}
