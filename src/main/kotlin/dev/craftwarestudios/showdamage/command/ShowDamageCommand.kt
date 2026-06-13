package dev.craftwarestudios.showdamage.command

import dev.craftwarestudios.showdamage.ShowDamage
import dev.craftwarestudios.showdamage.util.TextUtils.green
import org.bukkit.NamespacedKey
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import org.incendo.cloud.paper.LegacyPaperCommandManager

class ShowDamageCommand(manager: LegacyPaperCommandManager<CommandSender>) {
    init {
        val commandBuilder = manager.commandBuilder("showdamage", "sd")

        manager.command(commandBuilder
            .literal("reload-config")
            .permission(PERMISSION_RELOAD)
            .handler { ctx ->
                ShowDamage.instance.reloadConfiguration()
                ctx.sender().sendMessage(green("Config reloaded"))
            }
        )

        manager.command(commandBuilder
            .literal("disable-popup")
            .permission(PERMISSION_DISABLE_POPUP)
            .senderType(Player::class.java)
            .handler { ctx ->
                val container = ctx.sender().persistentDataContainer
                val current = container.get(PDC_VISIBILITY_KEY, PersistentDataType.INTEGER) ?: 1
                val toggled = -current

                container.set(PDC_VISIBILITY_KEY, PersistentDataType.INTEGER, toggled)
                ctx.sender().sendMessage(green("Visibility toggled to ", toggled))
            }
        )
    }

    companion object {
        const val PERMISSION_RELOAD = "showdamage.command.reloadconfig"
        const val PERMISSION_DISABLE_POPUP = "showdamage.command.disablepopup"

        val PDC_VISIBILITY_KEY = NamespacedKey(ShowDamage.instance, "showdamage.damage.visible")
    }
}