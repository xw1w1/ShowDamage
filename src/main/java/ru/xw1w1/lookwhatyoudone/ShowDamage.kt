package ru.xw1w1.lookwhatyoudone

import net.luckperms.api.LuckPerms
import net.luckperms.api.LuckPermsProvider
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import org.incendo.cloud.execution.ExecutionCoordinator
import org.incendo.cloud.paper.LegacyPaperCommandManager
import ru.xw1w1.lookwhatyoudone.api.ShowDamageAPI
import ru.xw1w1.lookwhatyoudone.api.ShowDamageProvider
import ru.xw1w1.lookwhatyoudone.api.renderer.ShowDamageRenderer
import ru.xw1w1.lookwhatyoudone.command.CommandShowDamage
import ru.xw1w1.lookwhatyoudone.config.Configuration
import ru.xw1w1.lookwhatyoudone.event.EntityDamageListener

class ShowDamage : JavaPlugin(), ShowDamageAPI {
    private lateinit var renderer: ShowDamageRenderer
    private lateinit var commandManager: LegacyPaperCommandManager<CommandSender>

    override fun onEnable() {
        commandManager = LegacyPaperCommandManager.createNative(
            this,
            ExecutionCoordinator.asyncCoordinator()
        )

        Configuration.loadDefaults()
        Configuration.init()

        ShowDamageProvider.register(this)
        CommandShowDamage.register(this.commandManager)

        server.pluginManager.registerEvents(EntityDamageListener(), this)

        this.renderer = ShowDamageRendererImpl()
    }

    override fun onDisable() {
        ShowDamageProvider.unregister()
        CommandShowDamage.unregister(this.commandManager)
    }

    companion object {
        @JvmStatic
        fun getInstance(): ShowDamage {
            return getPlugin(ShowDamage::class.java)
        }
    }

    override fun getLuckPerms(): LuckPerms {
        return LuckPermsProvider.get()
    }

    override fun renderer(): ShowDamageRenderer {
        return this.renderer
    }

    override fun commandManager(): LegacyPaperCommandManager<CommandSender> {
        return this.commandManager
    }

}