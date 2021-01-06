package com.github.ricetech.minecraftmanhuntplugin.commands;

import com.github.ricetech.minecraftmanhuntplugin.MinecraftManhuntPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StartGameCommand implements CommandExecutor {
    private MinecraftManhuntPlugin plugin;

    public StartGameCommand(MinecraftManhuntPlugin plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        int seconds;

        if (args.length != 1) {
            return false;
        }

        try {
            seconds = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            commandSender.sendMessage("Error: " + args[0] + " is not a number");
            return false;
        }

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), MinecraftManhuntPlugin.RESET_COMMAND_ALIAS);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), MinecraftManhuntPlugin.COUNTDOWN_COMMAND_ALIAS +
                " " + seconds);

        this.plugin.setGameInProgress(true);
        return true;
    }
}
