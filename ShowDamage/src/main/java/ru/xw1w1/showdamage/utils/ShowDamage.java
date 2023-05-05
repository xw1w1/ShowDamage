package ru.xw1w1.showdamage.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.jetbrains.annotations.NotNull;
import ru.xw1w1.showdamage.Main;

public class ShowDamage extends TextUtils {
    public void show(@NotNull String damage, @NotNull Location location, boolean critical ) {
        @NotNull ArmorStand armorStand = location.getWorld().spawn(location, ArmorStand.class);
        armorStand.setGravity(false);
        armorStand.setCustomNameVisible(true);
        armorStand.setCollidable(false);
        armorStand.setInvisible(true);
        armorStand.setInvulnerable(true);
        if (critical) {
            armorStand.customName(gradient("BE2510", "FD7348", component("✧ " + damage)));
        } else {
            armorStand.customName(gradient("F16666", "B79191", damage));
        }
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), armorStand::remove, 20L);
    }
}