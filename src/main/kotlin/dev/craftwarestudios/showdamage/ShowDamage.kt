package dev.craftwarestudios.showdamage

import dev.craftwarestudios.showdamage.command.ShowDamageCommand
import dev.craftwarestudios.showdamage.damage.DamageClusterCache
import dev.craftwarestudios.showdamage.listener.DamageListener
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import org.incendo.cloud.execution.ExecutionCoordinator
import org.incendo.cloud.paper.LegacyPaperCommandManager

class ShowDamage : JavaPlugin() {
    val clusterCache: DamageClusterCache = DamageClusterCache()
    val configuration: ShowDamageConfiguration = ShowDamageConfiguration()

    lateinit var commandManager: LegacyPaperCommandManager<CommandSender>

    override fun onEnable() {
        instance = this

        this.reloadConfiguration()
        this.commandManager = LegacyPaperCommandManager.createNative(
            instance, ExecutionCoordinator.simpleCoordinator()
        )

        ShowDamageCommand(commandManager)

        server.pluginManager.registerEvents(DamageListener(), this)
    }

    fun reloadConfiguration() {
        this.configuration.reloadConfiguration()
    }

    companion object {
        lateinit var instance: ShowDamage
            private set
    }
}
