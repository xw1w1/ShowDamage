package org.ttlzmc.showdamage

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent

object DamageEventHandler: Listener {
   @EventHandler
   fun onEntityDamage(event: EntityDamageEvent) {
       /* Notes: EntityDamageItemEvent exists, so we probably don't need to worry about items.
            Maybe entities like ItemFrames though.
            EntityCombustEvent exists, might be useful for alternative TNT handling?
       */
       // EntityDamageEvent(@NotNull Entity damagee, @NotNull EntityDamageEvent.DamageCause cause, @NotNull DamageSource damageSource, double damage)
       event.entity // Victim
       event.cause // Cause
       event.finalDamage // Damage after all reductions applied, (Armor?, Resistance Potion?)
       event.damage // Damage dealt
       event.damageSource // Damage source
       event.damageSource.damageLocation // Seperate damage locations
       event.damageSource.damageType // List of enum definitions
       event.damageSource.directEntity // What is a direct entity?
       event.damageSource.causingEntity // e.g. Player dealing damage
   }
}