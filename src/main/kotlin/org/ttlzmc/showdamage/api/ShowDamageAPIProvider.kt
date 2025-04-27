package org.ttlzmc.showdamage.api

import net.kyori.adventure.text.Component
import org.bukkit.entity.Display
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
                    DamageType.SINGLE -> Component.text(data.getDamageDealt())
                    DamageType.MULTI -> Component.text("g")
                    DamageType.MACE -> Component.text(data.getDamageDealt())
                    DamageType.ARROW -> Component.text(data.getDamageDealt())
                },
            )

            billboard = Display.Billboard.CENTER
            isInvisible = true
            isSeeThrough = settings.isSeeThrough
            settings.backgroundTransparency
        }

        return DamageDisplay(settings.visibilityRadius, display)
    }
}