package ru.xw1w1.showdamage;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import ru.xw1w1.showdamage.listeners.EntityDamageListener;
import ru.xw1w1.showdamage.utils.DamageData;

import java.io.File;
import java.util.*;

public final class Main extends JavaPlugin {

    private final static Map<Location, DamageData> locationDamageEntityMap = new LinkedHashMap<>();  // This is really difficult to name

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
    public static class MultiDamageSystem {
        public static void appendToData(Location key) {
            locationDamageEntityMap.get(key).append();
        }
        public static void putDataInMap(Location key, DamageData data) {
            locationDamageEntityMap.put(key, data);
        }
        public static void reduceSizeInData(Location key) {
            locationDamageEntityMap.get(key).reduce();
        }
        public static int getSize(Location key) {
            return locationDamageEntityMap.get(key).size();
        }
        public static Set<Location> keySet(){
            return locationDamageEntityMap.keySet();
        }
        public static void deleteEntry(Location key) {
            locationDamageEntityMap.remove(key);
        }
        public static int getCount(Location key) {
            return locationDamageEntityMap.get(key).count();
        }
        public static String getDamageDealt(Location key) {
            return locationDamageEntityMap.get(key).getDamageDealt();
        }
        public static boolean isDamagedBySword(Location key) {
            return locationDamageEntityMap.get(key).isDamagedBySword();
        }
    }


}
