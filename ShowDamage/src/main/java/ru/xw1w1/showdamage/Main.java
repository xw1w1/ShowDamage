package ru.xw1w1.showdamage;

import org.bukkit.plugin.java.JavaPlugin;
import ru.xw1w1.showdamage.commands.ReloadConfig;
import ru.xw1w1.showdamage.listeners.EntityDamageListener;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new EntityDamageListener(), this);
    }

    @Override
    public void onDisable() {

    }

    public static Main getInstance(){
        return Main.getPlugin(Main.class);
    }
}
