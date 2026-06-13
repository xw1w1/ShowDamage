package dev.craftwarestudios.showdamage

import dev.craftwarestudios.showdamage.dao.AttackClassification
import org.bukkit.damage.DamageSource
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.entity.Trident
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent

/**
 * This class is responsible for converting raw Bukkit events into the
 * internal attack descriptions.
 *
 * It analyzes the damage sources, attacking entities, damage causes
 * and held weapons in order to determine the most
 * appropriate [dev.craftwarestudios.showdamage.dao.AttackClassification].
 *
 * The resulting classification is later used for damage merging,
 * rendering and chat message formatting.
 *
 * @author xw1w1
 * @since 2.0
 */
object DamageClassifier {
    fun classify(event: EntityDamageByEntityEvent): AttackClassification {
        val source = event.damageSource
        val direct = source.directEntity ?: event.damager
        val causing = source.causingEntity ?: event.damager

        return when (event.cause) {
            EntityDamageEvent.DamageCause.PROJECTILE -> classifyProjectile(source, direct, causing)
            EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK -> AttackClassification(
                kind = AttackKind.SWORD_SWEEP,
                group = AttackGroup.SWING
            )
            EntityDamageEvent.DamageCause.ENTITY_EXPLOSION,
            EntityDamageEvent.DamageCause.BLOCK_EXPLOSION -> AttackClassification(
                kind = AttackKind.EXPLOSION,
                group = AttackGroup.EXPLOSION
            )
            EntityDamageEvent.DamageCause.ENTITY_ATTACK -> classifyDirectHit(source, causing)
            else -> classifyFallback(source, causing, direct)
        }
    }

    private fun classifyProjectile(source: DamageSource, direct: Entity, causing: Entity): AttackClassification {
        val damageTypeKey = source.damageType.key.key.lowercase()
        val directName = direct.type.name.lowercase()

        return when {
            direct is Trident || "trident" in directName || "trident" in damageTypeKey -> AttackClassification(
                kind = AttackKind.TRIDENT,
                group = AttackGroup.PROJECTILE
            )
            direct is Projectile || "projectile" in damageTypeKey || "arrow" in damageTypeKey -> AttackClassification(
                kind = AttackKind.PROJECTILE,
                group = AttackGroup.PROJECTILE
            )
            causing is Player && isTridentOrSpear(causing.inventory.itemInMainHand.type.name, damageTypeKey) -> classifyDirectHit(source, causing)
            else -> AttackClassification(
                kind = AttackKind.PROJECTILE,
                group = AttackGroup.PROJECTILE
            )
        }
    }

    private fun classifyDirectHit(source: DamageSource, causing: Entity): AttackClassification {
        val player = causing as? Player ?: return AttackClassification(
            kind = AttackKind.MELEE,
            group = AttackGroup.DIRECT
        )

        val heldName = player.inventory.itemInMainHand.type.name.uppercase()
        val damageTypeKey = source.damageType.key.key.lowercase()

        return when {
            isMaceSmash(heldName, damageTypeKey) -> AttackClassification(
                kind = AttackKind.MACE_SMASH,
                group = AttackGroup.MACE
            )
            isSword(heldName) -> AttackClassification(
                kind = AttackKind.MELEE,
                group = AttackGroup.SWING
            )
            isTridentOrSpear(heldName, damageTypeKey) -> AttackClassification(
                kind = if (heldName.contains("TRIDENT")) AttackKind.TRIDENT else AttackKind.SPEAR,
                group = AttackGroup.DIRECT
            )
            isAxe(heldName) -> AttackClassification(
                kind = AttackKind.AXE,
                group = AttackGroup.DIRECT
            )
            else -> AttackClassification(
                kind = AttackKind.MELEE,
                group = AttackGroup.DIRECT
            )
        }
    }

    private fun classifyFallback(source: DamageSource, causing: Entity, direct: Entity): AttackClassification {
        val damageTypeKey = source.damageType.key.key.lowercase()
        val directName = direct.type.name.lowercase()

        return when {
            "mace" in damageTypeKey -> AttackClassification(
                kind = AttackKind.MACE_SMASH,
                group = AttackGroup.MACE
            )
            "trident" in damageTypeKey -> AttackClassification(
                kind = AttackKind.TRIDENT,
                group = AttackGroup.PROJECTILE
            )
            "spear" in damageTypeKey || "spear" in directName -> AttackClassification(
                kind = AttackKind.SPEAR,
                group = AttackGroup.DIRECT
            )
            "projectile" in damageTypeKey || "arrow" in damageTypeKey -> AttackClassification(
                kind = AttackKind.PROJECTILE,
                group = AttackGroup.PROJECTILE
            )
            causing is Player && isSword(causing.inventory.itemInMainHand.type.name) -> AttackClassification(
                kind = AttackKind.MELEE,
                group = AttackGroup.SWING
            )
            else -> AttackClassification(
                kind = AttackKind.MELEE,
                group = AttackGroup.DIRECT
            )
        }
    }

    private fun isSword(materialName: String): Boolean = materialName.endsWith("_SWORD")

    private fun isAxe(materialName: String): Boolean = materialName.endsWith("_AXE")

    private fun isTridentOrSpear(materialName: String, damageTypeKey: String): Boolean {
        val upper = materialName.uppercase()
        return upper.contains("TRIDENT") || upper.contains("SPEAR") || damageTypeKey.contains("trident") || damageTypeKey.contains("spear")
    }

    private fun isMaceSmash(materialName: String, damageTypeKey: String): Boolean {
        return materialName.contains("MACE") || damageTypeKey.contains("mace")
    }
}
