package ru.xw1w1.showdamage.listeners;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;
import ru.xw1w1.showdamage.utils.ShowDamage;
import ru.xw1w1.showdamage.utils.TextUtils;

import java.text.DecimalFormat;

public class EntityDamageListener extends TextUtils implements Listener {
    @EventHandler
    private void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            if (event.getEntity() instanceof ArmorStand a && !a.isVisible()) {
                return;
            } else {
                @NotNull final DecimalFormat df = new DecimalFormat("0.00");
                @NotNull final String damage = df.format(event.getDamage());
                @NotNull final Location location = event.getEntity().getLocation();
                ShowDamage executor = new ShowDamage();
                executor.show(damage, location, event.isCritical());
            }
        }
    }
}