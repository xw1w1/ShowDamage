package dev.craftwarestudios.showdamage.damage

import dev.craftwarestudios.showdamage.dao.AttackClassification
import dev.craftwarestudios.showdamage.dao.DamageClusterTicket
import dev.craftwarestudios.showdamage.ShowDamage
import dev.craftwarestudios.showdamage.dao.DamageAmount
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap
import org.bukkit.Bukkit
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import java.util.concurrent.atomic.AtomicInteger

class DamageClusterCache {
    private val index: AtomicInteger = AtomicInteger(1)
    private val clusterCache: Int2ObjectLinkedOpenHashMap<DamageCluster> = Int2ObjectLinkedOpenHashMap()

    fun register(event: EntityDamageByEntityEvent, classification: AttackClassification, damage: DamageAmount, critical: Boolean, mergeRadius: Double): DamageClusterTicket? {
        val strategy = MultipleDamageMergeStrategy.from(classification) ?: return null
        val sourceId = (event.damageSource.causingEntity ?: event.damager).uniqueId
        val location = DamageCluster.createPopupLocation(event.entity)
        val mergeRadiusSquared = mergeRadius * mergeRadius
        val isSweepVictim = event.cause == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK

        synchronized(this.clusterCache) {
            val existing = this.clusterCache.values.firstOrNull {
                it.canMergeWith(location, sourceId, strategy, mergeRadiusSquared)
            }

            if (existing != null) {
                existing.absorb(location, damage, critical, isSweepVictim)
                return DamageClusterTicket(existing.id, false)
            }

            val cluster = DamageCluster(
                id = index.getAndIncrement(),
                anchor = location.clone(),
                sourceId = sourceId,
                damager = event.damager,
                mergeStrategy = strategy,
                classification = classification,
                critical = critical,
                damage = damage,
                sawSweepHit = isSweepVictim
            )
            this.clusterCache[cluster.id] = cluster
            return DamageClusterTicket(cluster.id, true)
        }
    }

    fun consume(id: Int): DamageCluster? = synchronized(this.clusterCache) {
        this.clusterCache.remove(id)
    }

    fun scheduleFinalization(id: Int, delayTicks: Long, action: (DamageCluster) -> Unit) {
        Bukkit.getScheduler().runTaskLater(ShowDamage.instance, Runnable {
            val cluster = consume(id) ?: return@Runnable
            action(cluster)
        }, delayTicks.coerceAtLeast(1L))
    }
}
