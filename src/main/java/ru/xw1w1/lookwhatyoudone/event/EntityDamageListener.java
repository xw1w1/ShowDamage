package ru.xw1w1.lookwhatyoudone.event;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;
import ru.xw1w1.lookwhatyoudone.api.ShowDamageProvider;
import ru.xw1w1.lookwhatyoudone.data.DamageData;
import ru.xw1w1.lookwhatyoudone.data.DamageType;
import ru.xw1w1.lookwhatyoudone.util.KotlinExtensions;

public class EntityDamageListener implements Listener {

    @SuppressWarnings("all")
    @EventHandler void onEntityDamage(@NotNull EntityDamageByEntityEvent e) {
        if (e.getEntity().getType().equals(EntityType.ITEM)) return;
        Entity source = e.getDamageSource().getCausingEntity();
        if (source.getType() != EntityType.PLAYER) return;
        Player damager = (Player) source;

        var weapon = damager.getInventory().getItemInMainHand();
        boolean critical = e.isCritical();

        DamageType damageType = DamageType.HAND;

        if (KotlinExtensions.isSword(weapon)) damageType = DamageType.SWORD;
        if (KotlinExtensions.isMace(weapon)) damageType = DamageType.MACE;

        if (KotlinExtensions.isProjectile(e.getDamageSource().getDirectEntity())) damageType = DamageType.PROJECTILE;

        DamageData data = new DamageData(damageType, weapon, e.getEntity(), damager, e.getDamage(), critical);

        ShowDamageProvider.get().renderer().render(data);
    }

}
