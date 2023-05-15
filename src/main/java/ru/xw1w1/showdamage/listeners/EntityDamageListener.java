package ru.xw1w1.showdamage.listeners;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;
import ru.xw1w1.showdamage.Main;
import ru.xw1w1.showdamage.utils.ShowDamage;
import ru.xw1w1.showdamage.utils.TextUtils;

import java.text.DecimalFormat;

public class EntityDamageListener extends TextUtils implements Listener {
    @EventHandler
    private void onEntityDamage(EntityDamageByEntityEvent event) {
        @NotNull final FileConfiguration config = Main.getInstance().getConfiguration();

        if (config.getBoolean("messages.damage-visible")) {
            @NotNull final DecimalFormat df = new DecimalFormat("0.00");
            @NotNull final String damage = df.format(event.getDamage());
            @NotNull final Location location = event.getEntity().getLocation();
            @NotNull final Location spawnLocation = new Location(location.getWorld(), location.getX(), location.getY() + (event.getEntity().getBoundingBox().getHeight()), location.getZ());


            if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
                @NotNull AbstractArrow projectile = (AbstractArrow) event.getDamager();

                @NotNull final Component eventMessage = hex(
                        config.getString("colors.accent.first"), single(components("[",
                                hex(config.getString("colors.accent.third"), "i"),
                                component("] "),
                                hex(config.getString("colors.accent.second"), event.getEntity().getName()),
                                component(" took "),
                                hex(config.getString("colors.accent.second"), damage, " HP"),
                                component(" damage"))));

                if (projectile.getShooter() instanceof Player player) {
                    Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
                        if (config.getBoolean("messages.damage-projectile-chat-messages")) {
                            player.sendMessage(eventMessage);
                        }
                    });
                }
            }

            ShowDamage.show(damage, spawnLocation, event.isCritical(), config, event.getDamager());
        }
    }
}