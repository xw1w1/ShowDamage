package ru.xw1w1.lookwhatyoudone;

import lombok.extern.java.Log;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TextDisplay;
import org.jetbrains.annotations.NotNull;
import ru.xw1w1.lookwhatyoudone.api.renderer.ShowDamageRenderer;
import ru.xw1w1.lookwhatyoudone.config.Colours;
import ru.xw1w1.lookwhatyoudone.config.Configuration;
import ru.xw1w1.lookwhatyoudone.data.DamageData;
import ru.xw1w1.lookwhatyoudone.data.DamageType;
import ru.xw1w1.lookwhatyoudone.util.StyleUtils;

import static ru.xw1w1.lookwhatyoudone.util.StyleUtils.*;

@Log
public class ShowDamageRendererImpl implements ShowDamageRenderer {
    @Override
    public void render(@NotNull DamageData damageData) {
        boolean seeThrough = Configuration.get().getBoolean("render.show-through-walls");
        long lifetime = Configuration.get().getLong("render.popup-lifetime");
        int transparency = Configuration.get().getInt("render.popup-background-transparency");

        final TextDisplay indicator = (TextDisplay) damageData.damaged().getWorld().spawnEntity(
                damageData.indicatorLocation(),
                EntityType.TEXT_DISPLAY
        );

        var dataComponent = this.dataToComponent(damageData);

        indicator.setVisibleByDefault(true);
        indicator.setSeeThrough(seeThrough);
        indicator.setBillboard(Display.Billboard.CENTER);
        indicator.setBackgroundColor(Color.fromARGB(
                transparency,
                0, 0, 0)
        );

        indicator.getLineWidth();

        indicator.text(dataComponent);

        if (damageData.type() == DamageType.PROJECTILE) {
            if (Configuration.chatMessagesEnabled()) {
                damageData.damageSource().sendMessage(this.renderProjectileChatMessage(damageData));
            }
            // will be added in future
            //if (Configuration.hotbarHeartsEnabled()) {
            //    damageData.damageSource().sendActionBar(damageData.formattedHearts());
            ///}
        } else {
            Bukkit.getOnlinePlayers().forEach(player -> {
                if (!Configuration.textVisibleFor(player)) {
                    player.hideEntity(ShowDamage.getInstance(), indicator);
                }
            });
        }

        Bukkit.getScheduler().runTaskLater(ShowDamage.getInstance(), indicator::remove, lifetime);
    }

    public @NotNull Component dataToComponent(@NotNull DamageData data) {
        Colours c = Configuration.colours();
        return switch (data.type()) {
            case MACE -> {
                var maceComponent = spaced(
                        text(c.maceBracketLeft()), text(data.formattedDamage()), text(c.maceBracketRight())
                );
                yield StyleUtils.gradient(c.maceFirst(), c.maceSecond(), maceComponent);
            }
            case SWORD -> {
                Component component = text(data.formattedDamage());
                component = gradient(c.defaultFirst(), c.defaultSecond(), component);
                if (data.critical()) {
                    component = spaced(text(c.critSign()), text(data.formattedDamage()));
                    component = gradient(c.critFirst(), c.critSecond(), component);
                }
                yield component;
            }
            case HAND, PROJECTILE -> gradient(c.defaultFirst(), c.defaultSecond(), data.formattedDamage());
        };
    }

    @Override
    public @NotNull Component renderProjectileChatMessage(@NotNull DamageData data) {
        if (data.type() != DamageType.PROJECTILE) {
            LOGGER.severe("ShowDamageRendererImpl#renderProjectileChatMessage(...) called with non-projectile damage data");
            return empty();
        }
        Colours c = Configuration.colours();
        return hex(
                c.accentFirst(), single(components("[",
                        hex(c.accentThird(), "i"),
                        component("] "),
                        hex(c.accentSecond(), data.damaged().getName()),
                        component(" took "),
                        hex(c.accentSecond(), data.formattedDamage(), " HP"),
                        component(" damage"))));
    }
}
