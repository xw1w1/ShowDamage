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
        double lift = 0.15;
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
            //if (config.getBoolean("messages.damage-animate")) {
//                Vector3f translation = getPreciseDirection(damager.getLocation().getYaw());
//                AxisAngle4f axisAngleRotMat = new AxisAngle4f();
//                Transformation transformation = new Transformation(translation, axisAngleRotMat, new Vector3f(1,1,1), axisAngleRotMat);
//                textDisplay.setInterpolationDuration(360);
//                textDisplay.setTransformation(transformation);
            //}
        }

        if (critical) {
            textDisplay.text(gradient(config.getString("colors.crit-damage.first"), config.getString("colors.crit-damage.second"), component(config.getString("colors.crit-sign.sign") + " " + damage)));
        } else {
            textDisplay.text(gradient(config.getString("colors.default-damage.first"), config.getString("colors.default-damage.second"), damage));
        }


        textDisplay.teleport(new Location(loc.getWorld(), loc.getX(), loc.getY() + lift, loc.getZ()));

        Bukkit.getScheduler().runTaskLater(Main.getInstance(), textDisplay::remove, lifetime);


    }
//    private static Vector3f getPreciseDirection(float yaw) { This doesn't work as intended.
//        float z = (float) Math.cos(Math.toRadians(yaw)) - 0.5f;
//        float x = (float) -(Math.sin(Math.toRadians(yaw)) + 0.5f);
//        System.out.println(z + " 0 " + x);
//        return new Vector3f(x, 0, z);
//    }
}

