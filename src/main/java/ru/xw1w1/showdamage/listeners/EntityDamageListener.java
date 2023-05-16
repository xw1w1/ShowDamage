package ru.xw1w1.showdamage.listeners;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;
import ru.xw1w1.showdamage.Main;
import ru.xw1w1.showdamage.utils.DamageData;
import ru.xw1w1.showdamage.utils.ShowDamage;
import static ru.xw1w1.showdamage.utils.TextUtils.*;

import java.text.DecimalFormat;
import java.util.Iterator;



public class EntityDamageListener implements Listener {

    private enum MultiDamageEndResult {
        EXPLOSION_DONE,
        SINGLE_ENTITY_ATTACK_DONE,
        SWEEP_ENTITY_ATTACK_DONE,
        NOT_DONE,

    }
    @EventHandler
    private void onEntityDamage(EntityDamageByEntityEvent event) {
        @NotNull final FileConfiguration config = Main.getInstance().getConfiguration();

        if (!config.getBoolean("messages.damage-visible", true)) return;

        @NotNull final DecimalFormat df = new DecimalFormat("0.00");
        @NotNull final String damage = df.format(event.getDamage());
        @NotNull final Location location = event.getEntity().getLocation();
        @NotNull final Location spawnLocation = new Location(location.getWorld(), location.getX(), location.getY() + (event.getEntity().getBoundingBox().getHeight()) + 0.15, location.getZ());

        if (event.getEntity().getType().equals(EntityType.DROPPED_ITEM)) return;

        switch (event.getCause()) {
            case PROJECTILE:
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
                        if (config.getBoolean("messages.damage-projectile-chat-messages", true)) {
                            player.sendMessage(eventMessage);
                        }
                    });
                }
                break;
            case ENTITY_ATTACK:
                if (event.getDamager() instanceof Player player && !isHoldingSword(player)) break;

            case ENTITY_SWEEP_ATTACK, ENTITY_EXPLOSION, BLOCK_EXPLOSION:

                if (!config.getBoolean("multiple-entity-count.enabled", true)) break;

                Location damageLocation = findKeyByDistance(location, config.getDouble("multiple-entity-count.distance", 10.0)); // First, try to find the first damageData entry
                if (damageLocation == null) {
                    createDamageData(location, event.getCause(), damage);
                    damageLocation = location;
                } else {
                    Main.MultiDamageSystem.appendToData(damageLocation);
                }

                final Location finalDamageLocation = damageLocation;
                Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                    int count = Main.MultiDamageSystem.getCount(finalDamageLocation); // minus 1 to consider only the other entities, not the main one
                    Main.MultiDamageSystem.reduceSizeInData(finalDamageLocation);
                    MultiDamageEndResult endResult = getDamageResult(finalDamageLocation, event.getCause(), count);

                    switch (endResult) {
                        case SINGLE_ENTITY_ATTACK_DONE -> ShowDamage.show(damage, spawnLocation, event.isCritical(), config, event.getDamager());
                        case SWEEP_ENTITY_ATTACK_DONE -> {
                            String mainDamage = Main.MultiDamageSystem.getDamageDealt(finalDamageLocation);
                            ShowDamage.show( mainDamage + " + " + (count-1) + "X" + damage, spawnLocation, event.isCritical(), config, event.getDamager());
                        }
                        case EXPLOSION_DONE -> ShowDamage.show(count + "X" + damage, spawnLocation, event.isCritical(), config, event.getDamager());

                    }

                    if (!endResult.equals(MultiDamageEndResult.NOT_DONE)) Main.MultiDamageSystem.deleteEntry(finalDamageLocation);

                }, 1L);
                return;
        }
        ShowDamage.show(damage, spawnLocation, event.isCritical(), config, event.getDamager());
    }
    private static boolean isHoldingSword(Player player) {
        boolean sword = false;
        switch (player.getInventory().getItemInMainHand().getType()) { // enhanced switch statement
            case STONE_SWORD, DIAMOND_SWORD, GOLDEN_SWORD, IRON_SWORD, NETHERITE_SWORD, WOODEN_SWORD ->
                    sword = true;
        }
        return sword;
    }
    private static Location findKeyByDistance(Location currentLocation, double distance){
        Iterator<Location> it = Main.MultiDamageSystem.keySet().iterator();
        Location damageLocation = null;
        boolean found = false;
        while (it.hasNext() && !found) {
            damageLocation = it.next();
            if (damageLocation.getWorld().equals(currentLocation.getWorld())) {
                if (damageLocation.distance(currentLocation) <= distance) { // If the current location is within 10 (default) blocks of the first hit.
                    found = true;
                }
            }
        }
        return damageLocation;
    }
    private static void createDamageData(Location damageLocation, EntityDamageEvent.DamageCause cause, String damageDealt) {
        DamageData damageData;
        if (cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            damageData = new DamageData(true, damageDealt);
        } else {
            damageData = new DamageData(false);

        }
        damageData.append();
        Main.MultiDamageSystem.putDataInMap(damageLocation, damageData);
    }

    private static MultiDamageEndResult getDamageResult(Location damageLocation, EntityDamageEvent.DamageCause cause, int count) {
        if (Main.MultiDamageSystem.getSize(damageLocation) == 0) {  // Reduction to 0 means there is no more entities hit as the final task was completed.
            if (count == 1) { // A maximum count of 1 means that the final task was completed but only one entity was hit.
                return MultiDamageEndResult.SINGLE_ENTITY_ATTACK_DONE;
            }else {
                if (Main.MultiDamageSystem.isDamagedBySword(damageLocation)){ // Damage was dealt by sword but more than one entity was hit, this implies a sweep attack.
                    return MultiDamageEndResult.SWEEP_ENTITY_ATTACK_DONE;
                }else { // Damage was dealt to multiple entities but not by a sword, this implies an explosion.
                    return MultiDamageEndResult.EXPLOSION_DONE;
                }
            }
        }
        return MultiDamageEndResult.NOT_DONE;
    }

}