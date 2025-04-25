package org.ttlzmc.showdamage.api

import net.kyori.adventure.text.Component
import org.bukkit.entity.TextDisplay
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.annotations.ApiStatus
import org.ttlzmc.showdamage.ShowDamage
import org.ttlzmc.showdamage.api.datatypes.DamageData
import org.ttlzmc.showdamage.api.datatypes.DamageType
import org.ttlzmc.showdamage.api.datatypes.DisplaySettings

@ApiStatus.Internal
object ShowDamageAPIProvider : ShowDamageAPI {

    override fun getPlugin(): JavaPlugin {
        return ShowDamage.getInstance()
    }

    override fun createDisplay(data: DamageData, settings: DisplaySettings): DamageDisplay {
        val display = data.getLocation().world.spawn(data.getLocation(), TextDisplay::class.java).apply {
            text(
                when (settings.damageType) {
                    DamageType.SINGLE -> Component.text("g")
                    DamageType.MULTI -> Component.text("g")
                    DamageType.MACE -> Component.text("g")
                    DamageType.ARROW -> Component.text("g")
                }
            )

            isInvisible = true
            isSeeThrough = settings.isSeeThrough
            settings.backgroundTransparency
        }

        return DamageDisplay(settings.visibilityRadius, display)
    }
}