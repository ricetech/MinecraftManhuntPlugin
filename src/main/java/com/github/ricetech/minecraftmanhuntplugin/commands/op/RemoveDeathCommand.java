package com.github.ricetech.minecraftmanhuntplugin.commands.op;

import com.github.ricetech.minecraftmanhuntplugin.MinecraftManhuntPlugin;
import com.github.ricetech.minecraftmanhuntplugin.data.ScoreKeeper;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RemoveDeathCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length != 1) {
            return false;
        }

        Player p = Bukkit.getPlayerExact(args[0]);

        if (p == null) {
            MinecraftManhuntPlugin.sendErrorMsg(sender, "Target player does not exist or is offline.");
            return true;
        }

        ScoreKeeper.removeDeath(p);

        return true;
    }
}
