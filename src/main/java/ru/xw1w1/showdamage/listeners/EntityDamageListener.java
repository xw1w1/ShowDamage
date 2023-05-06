package ru.xw1w1.showdamage.listeners;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.*;
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
        if (Main.getInstance().getConfiguration().getBoolean("messages.damage-visible")) {

            @NotNull final DecimalFormat df = new DecimalFormat("0.00");
            @NotNull final String damage = df.format(event.getDamage());
            @NotNull final Location eventLocation = event.getEntity().getLocation();
            @NotNull final ShowDamage executor = new ShowDamage();
            @NotNull final Location location = new Location(eventLocation.getWorld(), eventLocation.getX() + 100, eventLocation.getY(), eventLocation.getZ());

            @NotNull final Component eventMessage = hex(
                    Main.getInstance().getConfiguration().getString("colors.accent.first"), single(components("[",
                    hex(Main.getInstance().getConfiguration().getString("colors.accent.third"), "i"),
                            component("] "),
                    hex(Main.getInstance().getConfiguration().getString("colors.accent.second"), event.getEntity().getName()),
                            component(" took "),
                    hex(Main.getInstance().getConfiguration().getString("colors.accent.second"), damage, " HP"),
                            component(" damage"))));

            if (event.getDamager() instanceof Player) {
                if (event.getEntity() instanceof ArmorStand a && !a.isVisible()) return;
            }

            if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
                @NotNull AbstractArrow projectile = (AbstractArrow) event.getDamager();
                if (projectile.getShooter() instanceof Player player) {
                    Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
                        if (Main.getInstance().getConfiguration().getBoolean("messages.damage-projectile-chat-messages")) {
                            player.sendMessage(eventMessage);
                        }
                    });
                }
            }

            executor.show(damage, location, event.isCritical());
        }
    }
}