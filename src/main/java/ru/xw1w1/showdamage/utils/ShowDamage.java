package ru.xw1w1.showdamage.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.jetbrains.annotations.NotNull;
import ru.xw1w1.showdamage.Main;

public class ShowDamage extends TextUtils {
    public static void show(@NotNull String damage, @NotNull Location loc, boolean critical, FileConfiguration config, Player player) {
        @NotNull TextDisplay textDisplay = (TextDisplay) loc.getWorld().spawnEntity(loc, EntityType.TEXT_DISPLAY);
        textDisplay.setGravity(false);
        textDisplay.setBillboard(Display.Billboard.CENTER);
        textDisplay.setCustomNameVisible(true);
        textDisplay.setInvulnerable(true);


        if (player != null) { // Player -> Entity appears only to player, Entity -> Entity appears to everyone
            textDisplay.setVisibleByDefault(false); // This might break in the future!! Check @ApiStatus.Experimental
            player.showEntity(Main.getInstance(), textDisplay); // Might break: Check @ApiStatus.Experimental
        }

        if (config.getBoolean("damage.show-through-walls")) {
            textDisplay.setSeeThrough(true);
        }



        textDisplay.teleport(new Location(loc.getWorld(), loc.getX() - 100, loc.getY() + 1.93, loc.getZ()));

        if (critical) {
            textDisplay.text(gradient(config.getString("colors.crit-damage.first"), config.getString("colors.crit-damage.second"), component(config.getString("colors.crit-sign.sign") + " " + damage)));
        } else {
            textDisplay.text(gradient(config.getString("colors.default-damage.first"), config.getString("colors.default-damage.second"), damage));
        }
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), textDisplay::remove, 25L);
    }
}