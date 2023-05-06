package ru.xw1w1.showdamage.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.xw1w1.showdamage.Main;
import ru.xw1w1.showdamage.utils.ShowDamage;
import ru.xw1w1.showdamage.utils.TextUtils;

public class ShowDamageCommand extends TextUtils implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(red("Usage:  /showdamage debugdamage|reloadconfig"));
        }
        if (args.length >= 1) {
            if (args[0].equals("debugdamage")) {
                ShowDamage showDamage = new ShowDamage();
                showDamage.show("12.25", ((Player) sender).getLocation(), false);
            }
        }
        return true;
    }
}
