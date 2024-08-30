package ru.xw1w1.lookwhatyoudone.command

import net.luckperms.api.node.types.MetaNode
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.incendo.cloud.paper.LegacyPaperCommandManager
import ru.xw1w1.lookwhatyoudone.api.ShowDamageProvider
import ru.xw1w1.lookwhatyoudone.config.Configuration

object CommandShowDamage {

    @JvmStatic
    fun register(manager: LegacyPaperCommandManager<CommandSender>) {
        val builder = manager.commandBuilder("showdamage", "sd")

        manager.command(
            builder.literal("toggle-popup")
                .senderType(Player::class.java)
                .handler { this.handleTogglePopup(it.sender()) }
        )
    }

    @JvmStatic
    fun unregister(manager: LegacyPaperCommandManager<CommandSender>) {
        manager.deleteRootCommand("showdamage")
    }

    private fun handleTogglePopup(sender: Player) {
        val previous = Configuration.checkShowDamageVisibilityForPlayer(sender)
        ShowDamageProvider.get().luckPerms.userManager.modifyUser(sender.uniqueId) { user ->
            val node = MetaNode.builder(Configuration.VISIBILITY_META_KEY, (!previous).toString()).build()
            user.data().clear { it is MetaNode && it.metaKey == Configuration.VISIBILITY_META_KEY }
            user.data().add(node)
        }
    }

}