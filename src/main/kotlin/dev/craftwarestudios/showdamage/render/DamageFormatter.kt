package dev.craftwarestudios.showdamage.render

import dev.craftwarestudios.showdamage.AttackKind
import dev.craftwarestudios.showdamage.ShowDamage
import dev.craftwarestudios.showdamage.dao.DamageAmount
import dev.craftwarestudios.showdamage.util.TextUtils
import dev.craftwarestudios.showdamage.util.TextUtils.gradient
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import org.bukkit.entity.Player

object DamageFormatter {
    fun createCompensatedDamageComponent(damage: DamageAmount): Component? {
        val compensated = damage.mitigated
        if (compensated <= 0.0) return null

        val shield = "\uD83D\uDEE1" // xD
        // probably I need to add config ^ for this one too
        val colFirst = "44577E"
        val colSecond = "485273"

        val configuration = ShowDamage.instance.configuration
        val hearts = configuration.getBoolean("display-settings.show-damage-as-hearts", false)
        val baseValue = DamageAmount(compensated, compensated).format(hearts)
        // pretty hacky and weird way to do it, but why not?

        return gradient(colFirst, colSecond, "$shield -$baseValue")
    }

    fun createClusterComponentInlined(
        damage: DamageAmount,
        splashDamage: DamageAmount? = null,
        splashTargetsCount: Int,
        displayKind: AttackKind,
        isCritical: Boolean
    ): Component {
        val configuration = ShowDamage.instance.configuration
        val hearts = configuration.getBoolean("display-settings.show-damage-as-hearts", false)
        val baseValue = damage.format(hearts)
        val splashValue = splashDamage?.format(hearts)

        val prefix = badgeSymbol(displayKind)

        val critFirst = configuration.getString("colors.crit-damage.first", "BE2510")
        val critSecond = configuration.getString("colors.crit-damage.second", "FD7348")
        val defaultFirst = configuration.getString("colors.default-damage.first", "F16666")
        val defaultSecond = configuration.getString("colors.default-damage.second", "B79191")
        val critSign = configuration.getString("prefixes.crit-prefix", "X")

        val damageString = if (splashValue != null) "$baseValue + $splashValue/x${splashTargetsCount}" else baseValue
        val damageComponent = if (isCritical) {
            val string = if (prefix != null) "$prefix $critSign $damageString" else "$critSign $damageString"
            gradient(critFirst, critSecond, string)
        } else {
            val string = if (prefix != null) "$prefix $damageString" else damageString
            gradient(defaultFirst, defaultSecond, string)
        }

        return damageComponent
    }

    fun createClusterComponentMultiline(
        damage: DamageAmount,
        splashDamage: DamageAmount? = null,
        splashTargetsCount: Int,
        displayKind: AttackKind,
        isCritical: Boolean
    ): Component {
        val configuration = ShowDamage.instance.configuration
        val hearts = configuration.getBoolean("display-settings.show-damage-as-hearts", false)
        val baseValue = damage.format(hearts)
        val subLine = splashDamage?.format(hearts)

        val prefix = badgeSymbol(displayKind)

        val critFirst = configuration.getString("colors.crit-damage.first", "BE2510")
        val critSecond = configuration.getString("colors.crit-damage.second", "FD7348")
        val defaultFirst = configuration.getString("colors.default-damage.first", "F16666")
        val defaultSecond = configuration.getString("colors.default-damage.second", "B79191")
        val critSign = configuration.getString("prefixes.crit-prefix", "X")

        val line0 = if (isCritical) {
            val string = if (prefix != null) "$prefix $critSign $baseValue" else "$critSign $baseValue"
            gradient(critFirst, critSecond, string)
        } else {
            val string = if (prefix != null) "$prefix $baseValue" else baseValue
            gradient(defaultFirst, defaultSecond, string)
        }
        val sl0 = if (subLine == null) null else {
            gradient(defaultFirst, defaultSecond, "$subLine/x${splashTargetsCount}")
        }

        val components = if (sl0 == null) arrayOf(line0) else arrayOf(line0, sl0)
        return TextUtils.single(JoinConfiguration.newlines(), *components)
    }

    fun createProjectileComponentMessage(victimName: String, damage: DamageAmount): Component {
        val config = ShowDamage.instance.configuration
        val hearts = config.getBoolean("display-settings.show-damage-as-hearts", false)
        val damageString = damage.format(hearts)

        val accentFirst = config.getString("colors.message.first", "25af46")
        val accentSecond = config.getString("colors.message.second", "c3c3c3")
        val accentThird = config.getString("colors.message.elements", "2a4858")

        return TextUtils.hex(
            accentFirst,
            TextUtils.single(
                *TextUtils.components(
                    "[", TextUtils.hex(accentSecond, "i"), "]",
                    TextUtils.hex(accentThird, victimName), " took ",
                    TextUtils.hex(accentThird, damageString, "HP"),
                    " damage."
                )
            )
        )
    }

    private fun badgeSymbol(kind: AttackKind): String? {
        val config = ShowDamage.instance.configuration
        if (!config.getBoolean("prefixes.show-attack-prefixes")) return null

        val badge = when (kind) {
            AttackKind.SWORD_SWEEP -> config.getString("prefixes.sweep-prefix", kind.defaultBadge ?: "")
            AttackKind.EXPLOSION -> config.getString("prefixes.explosion-prefix", kind.defaultBadge ?: "")
            AttackKind.MACE_SMASH -> config.getString("prefixes.mace-prefix", kind.defaultBadge ?: "")
            AttackKind.TRIDENT -> config.getString("prefixes.trident-prefix", kind.defaultBadge ?: "")
            AttackKind.SPEAR -> config.getString("prefixes.spear-prefix", kind.defaultBadge ?: "")
            AttackKind.PROJECTILE -> config.getString("prefixes.projectile-prefix", kind.defaultBadge ?: "")
            else -> kind.defaultBadge
        }

        return if (badge.isNullOrEmpty()) null else badge
    }
}