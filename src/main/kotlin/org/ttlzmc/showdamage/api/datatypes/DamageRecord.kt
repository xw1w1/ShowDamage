package org.ttlzmc.showdamage.api.datatypes

import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.jetbrains.annotations.UnmodifiableView
import java.util.*

/**
 * A record of damage data. Used to collect all the entities involved in a single player hit.
 */
@Suppress("MemberVisibilityCanBePrivate")
class DamageRecord(
    private val damageType: DamageType,
    private val damageDealerUUID: UUID,
    private val creationTick: Long
) {
    private var isDone: Boolean = false
    private val affectedEntities: ArrayList<Entity> = arrayListOf()

    fun checkState() = isDone
    fun markReady() { this.isDone = true }

    fun checkDealerValidity(): Boolean {
        return Bukkit.getOfflinePlayer(damageDealerUUID).isOnline
    }

    fun getDealer(): Player {
        val damageDealer = Bukkit.getOfflinePlayer(damageDealerUUID)
        val dealer = damageDealer.player
        if (dealer != null && checkDealerValidity()) return damageDealer.player!!
        else throw NullPointerException("Damage dealer is not valid, entry will be deleted.")
    }

    fun getDealerUUID(): UUID {
        return this.damageDealerUUID
    }

    fun getType(): DamageType {
        return this.damageType
    }

    fun getTick(): Long {
        return creationTick
    }

    fun addTarget(entity: Entity) {
        this.affectedEntities.add(entity)
    }

    fun getTargets(): @UnmodifiableView List<Entity> {
        return affectedEntities
    }
}