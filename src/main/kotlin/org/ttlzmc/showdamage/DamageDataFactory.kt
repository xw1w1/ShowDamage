package org.ttlzmc.showdamage

import org.bukkit.entity.Entity
import org.ttlzmc.showdamage.api.datatypes.DamageRecord
import org.ttlzmc.showdamage.api.datatypes.DamageType
import java.util.UUID

object DamageDataFactory {
    private val records: MutableList<DamageRecord> = mutableListOf()

    fun createOrCompute(owner: UUID, type: DamageType, target: Entity, tick: Long) {
        val existingRecord = records.find { it.getDealerUUID() == owner && it.getTick() == tick && !it.checkState() }
        if (existingRecord != null) {
            existingRecord.addTarget(target)
        } else {
            val newRecord = DamageRecord(type, owner, tick)
            newRecord.addTarget(target)
            records.add(newRecord)
        }
    }

    fun getRecordsForTick(tick: Long): List<DamageRecord> {
        return records.filter { it.getTick() == tick }
    }

    fun purgeRecords(tick: Long) {
        records.removeIf { it.getTick() == tick }
    }
}