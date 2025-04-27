package org.ttlzmc.showdamage

import com.destroystokyo.paper.event.server.ServerTickEndEvent
import com.destroystokyo.paper.event.server.ServerTickStartEvent
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.ttlzmc.showdamage.api.datatypes.DamageType

object DamageEventHandler: Listener {
    private var currentTick = 0L

    @EventHandler
    fun onTickStart(ignored: ServerTickStartEvent) {
        currentTick++
    }

    @EventHandler
    fun onEntityDamage(event: EntityDamageByEntityEvent) {
        var dealer = event.damager
        if (dealer is Projectile && dealer.shooter is Player) dealer = dealer.shooter as Player
        if (event.entity.type == EntityType.ITEM) return

        if (event.cause == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) {
            DamageDataFactory.createOrCompute(dealer.uniqueId, DamageType.MULTI, event.entity, currentTick)
        }
    }

    @EventHandler
    fun onTickEnd(ignored: ServerTickEndEvent) {
        val records = DamageDataFactory.getRecordsForTick(currentTick)
        records.forEach { record ->
            if (!record.checkState()) {
                try {
                    record.markReady()
                    val dealer = record.getDealer()
                    val targets = record.getTargets()
                    targets.forEach { entity ->
                        dealer.sendMessage("Вы поразили ${entity.name} с помощью Sweeping Edge!")
                    }
                } catch (ignored: NullPointerException) {
                    // NPE from DamageRecord#getDealer
                }
            }
        }
        DamageDataFactory.purgeRecords(currentTick)
    }
}