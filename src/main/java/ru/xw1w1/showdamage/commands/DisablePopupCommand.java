package ru.xw1w1.showdamage.commands;

import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import ru.xw1w1.showdamage.Main;
import static ru.xw1w1.showdamage.utils.TextUtils.*;

public class DisablePopupCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;

        NamespacedKey key = new NamespacedKey(Main.getInstance(), "damage-visible");
        PersistentDataContainer container = player.getPersistentDataContainer();
        Integer data;

        if (container.has(key)) {
            data = container.get(key, PersistentDataType.INTEGER);
        }else {
            data = 1; // This gets inverted to be -1
        }

        assert data != null;
        player.sendMessage(green(component("Toggled popup visibility to " + -data)));



        container.set(key, PersistentDataType.INTEGER, -data); // Toggle

        return true;


    }
}
