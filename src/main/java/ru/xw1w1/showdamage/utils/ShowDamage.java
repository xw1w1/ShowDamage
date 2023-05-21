package ru.xw1w1.showdamage.utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.jetbrains.annotations.NotNull;
import ru.xw1w1.showdamage.Main;
import static ru.xw1w1.showdamage.utils.TextUtils.*;


public class ShowDamage {
    public static void show(@NotNull DamageData damage, @NotNull Location loc, boolean critical, @NotNull FileConfiguration config, @NotNull Entity damager) {
        @NotNull CustomEntityHandler CEH = new CustomEntityHandler(config, loc);
        final long lifetime = config.getLong("damage.popup-lifetime", 25L);

        CEH.setDefaults(damager);

        if (critical) {
            CEH.text(gradient(config.getString("colors.crit-damage.first"), config.getString("colors.crit-damage.second"), component(config.getString("colors.crit-sign.sign") + " " + damage.valueOf())));
        } else {
            CEH.text(gradient(config.getString("colors.default-damage.first"), config.getString("colors.default-damage.second"), damage.valueOf()));
        }

        Bukkit.getScheduler().runTaskLater(Main.getInstance(), CEH::remove, lifetime);


    }

}

