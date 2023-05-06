package ru.xw1w1.showdamage;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import ru.xw1w1.showdamage.listeners.EntityDamageListener;
import java.io.File;

public final class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new EntityDamageListener(), this);
        //getCommand("showdamage").setExecutor(new ShowDamageCommand());
        //getCommand("showdamage").setTabCompleter(new ShowDamageCommandTabCompletion());
        Bukkit.getLogger().info("[ShowDamage] Running ShowDamage v1.1");
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("[ShowDamage] Bye!");
    }

    public static @NotNull Main getInstance(){
        return Main.getPlugin(Main.class);
    }

    public @NotNull FileConfiguration getConfiguration() {
        final @NotNull File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            saveDefaultConfig();
        }
        return YamlConfiguration.loadConfiguration(configFile);
    }
}
