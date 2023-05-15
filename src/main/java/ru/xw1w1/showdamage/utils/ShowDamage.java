package ru.xw1w1.showdamage.utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.jetbrains.annotations.NotNull;
import ru.xw1w1.showdamage.Main;

public class ShowDamage extends TextUtils {
    public static void show(@NotNull String damage, @NotNull Location loc, boolean critical, @NotNull FileConfiguration config, @NotNull Entity damager) {
        @NotNull TextDisplay textDisplay = (TextDisplay) loc.getWorld().spawnEntity(loc, EntityType.TEXT_DISPLAY);
        final long lifetime = config.getLong("messages.damage-popup-lifetime");

        textDisplay.setGravity(true);
        textDisplay.setBillboard(Display.Billboard.CENTER); // Aligns the text to face towards player
        textDisplay.setCustomNameVisible(true);
        textDisplay.setInvulnerable(true);


        if (damager instanceof Player player && !config.getBoolean("messages.damage-visible-to-all")) {
            textDisplay.setVisibleByDefault(false); // This might break in the future!! Check @ApiStatus.Experimental
            player.showEntity(Main.getInstance(), textDisplay); // Might break: Check @ApiStatus.Experimental
        }

        if (config.getBoolean("messages.damage-show-through-walls")) {
            textDisplay.setSeeThrough(true);

        }

        textDisplay.teleport(loc);
        if (critical) {
            textDisplay.text(gradient(config.getString("colors.crit-damage.first"), config.getString("colors.crit-damage.second"), component(config.getString("colors.crit-sign.sign") + " " + damage)));
        } else {
            textDisplay.text(gradient(config.getString("colors.default-damage.first"), config.getString("colors.default-damage.second"), damage));
        }

        Bukkit.getScheduler().runTaskLater(Main.getInstance(), textDisplay::remove, lifetime);


    }
}

