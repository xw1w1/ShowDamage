package ru.xw1w1.showdamage.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import ru.xw1w1.showdamage.Main;

import java.util.Collection;
import java.util.Objects;

public class CustomEntityHandler {
    final private Entity textEntity;
    final private FileConfiguration config;
    final private Location spawnLoc;
    final private boolean legacy_mode;

    public CustomEntityHandler(FileConfiguration config, Location loc) {
        this.legacy_mode = config.getBoolean("damage.legacy-mode", false);
        this.config = config;
        this.spawnLoc = loc;

        if (legacy_mode) {
            textEntity = loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND, CreatureSpawnEvent.SpawnReason.CUSTOM, (entity) -> {
                ((ArmorStand) entity).setMarker(true);
                ((ArmorStand) entity).setInvisible(true);
            });
        }else {
            textEntity = loc.getWorld().spawnEntity(loc, EntityType.TEXT_DISPLAY);
        }
    }
    public void text(Component text) {
        if (legacy_mode) { textEntity.customName(text); return; }
        ((TextDisplay) textEntity).text(text);
    }
    public void remove() {
        textEntity.remove();
    }

    public void setDefaults(Entity damager) {
        textEntity.setInvulnerable(true);
        textEntity.setCustomNameVisible(true);
        textEntity.setGravity(false);

        if (!legacy_mode) {
            if (config.getBoolean("damage.show-through-walls", true)) {
                ((TextDisplay) textEntity).setSeeThrough(true);
            }
            // Ignore warning
            ((TextDisplay) textEntity).setDefaultBackground(false);
            ((TextDisplay) textEntity).setBackgroundColor(Color.fromARGB(config.getInt("messages.popup-background-transparency", 64), 0, 0, 0));
            ((TextDisplay) textEntity).setBillboard(Display.Billboard.CENTER); // Aligns the text to face towards player
        }

        if (damager instanceof Player player && !config.getBoolean("damage.visible-to-all", false)) {
            textEntity.setVisibleByDefault(false); // This might break in the future!! Check @ApiStatus.Experimental
            player.showEntity(Main.getInstance(), textEntity); // Might break: Check @ApiStatus.Experimental
        }

        if (config.getBoolean("damage.allow-players-to-disable", true)){ // an optimization might exist here for visible to all
            Collection<Player> players = spawnLoc.getNearbyPlayers(config.getInt("damage.hide-entity-radius", 32));
            for (Player player : players) {
                if (!checkPlayerVisibilityConfig(player)) {
                    player.hideEntity(Main.getInstance(), textEntity); // Might break in the future!!
                }
            }
        }
    }
    private static boolean checkPlayerVisibilityConfig(Player player) {
        NamespacedKey key = new NamespacedKey(Main.getInstance(), "damage-visible");
        PersistentDataContainer container = player.getPersistentDataContainer();

        if (!container.has(key)) return true; // By default, player is visible.

        return Objects.requireNonNull(player.getPersistentDataContainer().get(key, PersistentDataType.INTEGER)) > 0;

    }

}
