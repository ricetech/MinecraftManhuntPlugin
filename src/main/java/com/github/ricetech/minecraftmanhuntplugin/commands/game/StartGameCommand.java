package com.github.ricetech.minecraftmanhuntplugin.commands.game;

import com.github.ricetech.minecraftmanhuntplugin.MinecraftManhuntPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class StartGameCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        int seconds;

        if (args.length < 1 || args.length > 2) {
            return false;
        }

        if (MinecraftManhuntPlugin.isGameInProgress) {
            MinecraftManhuntPlugin.sendErrorMsg(sender, "A game is already in progress.");
        }

        try {
            seconds = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            MinecraftManhuntPlugin.sendErrorMsg(sender, args[0] + " is not a number");
            return false;
        }

        if (args.length == 2 && !args[1].toLowerCase().matches("^true$|^false$")) {
            MinecraftManhuntPlugin.sendErrorMsg(sender, "Argument for 'reset' must be 'true' or 'false'");
            return false;
        }

        if (args.length == 1 || args[1].equalsIgnoreCase("false")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), MinecraftManhuntPlugin.RESET_COMMAND_ALIAS);
        }
        ListTeamsCommand.listTeams(false, null);

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), MinecraftManhuntPlugin.COUNTDOWN_COMMAND_ALIAS +
                " " + seconds);

        MinecraftManhuntPlugin.isGameInProgress = true;
        return true;
    }
}
