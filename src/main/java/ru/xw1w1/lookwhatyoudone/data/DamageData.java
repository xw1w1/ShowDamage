package ru.xw1w1.lookwhatyoudone.data;

import java.text.DecimalFormat;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public record DamageData(
        @NotNull DamageType type, @NotNull ItemStack weapon, @NotNull Entity damaged,
        @NotNull Player damageSource, double damage, boolean critical
) {

    public @NotNull String formattedDamage() {
        return (new DecimalFormat("0.00")).format(this.damage);
    }

    public @NotNull String formattedHearts() {
        // Count of small (1/2) hearts           full heart
        int smallHearts = Math.floorMod((long) this.damage, 2);
        int bigHearts = (int) (damage - smallHearts)/2;
        return "❤".repeat(Math.max(0, bigHearts))
                .concat("♥".repeat(Math.max(0, smallHearts)));
    }

    public @NotNull Location indicatorLocation() {
        return damaged.getLocation().add(0, 1, 0);
    }

    @Override
    public String toString() {
        return this.formattedDamage();
    }

}
