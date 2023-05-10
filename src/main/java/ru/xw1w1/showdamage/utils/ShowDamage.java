package ru.xw1w1.showdamage.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.jetbrains.annotations.NotNull;
import ru.xw1w1.showdamage.Main;

public class ShowDamage extends TextUtils {
    public void show(@NotNull String damage, @NotNull Location loc, boolean critical, FileConfiguration config) {
        @NotNull ArmorStand armorStand = loc.getWorld().spawn(loc, ArmorStand.class);
        armorStand.setInvisible(true);
        armorStand.setGravity(false);
        armorStand.setCustomNameVisible(true);
        armorStand.setCollidable(false);
        armorStand.setInvulnerable(true);
        armorStand.setCanTick(false);
        armorStand.setMarker(true);
        armorStand.teleport(new Location(loc.getWorld(), loc.getX() - 100, loc.getY() + 1.93, loc.getZ()));

        if (critical) {
            armorStand.customName(gradient(config.getString("colors.crit-damage.first"), config.getString("colors.crit-damage.second"), component(config.getString("colors.crit-sign.sign") + " " + damage)));
        } else {
            armorStand.customName(gradient(config.getString("colors.default-damage.first"), config.getString("colors.default-damage.second"), damage));
        }
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), armorStand::remove, 25L);
    }
}