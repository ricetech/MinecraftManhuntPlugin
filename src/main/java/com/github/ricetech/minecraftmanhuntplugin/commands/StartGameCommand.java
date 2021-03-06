package com.github.ricetech.minecraftmanhuntplugin.commands;

import com.github.ricetech.minecraftmanhuntplugin.MinecraftManhuntPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StartGameCommand implements CommandExecutor {
    private final MinecraftManhuntPlugin manhuntPlugin;

    public StartGameCommand(MinecraftManhuntPlugin manhuntPlugin) {
        this.manhuntPlugin = manhuntPlugin;
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        int seconds;

        if (args.length < 1 || args.length > 2) {
            return false;
        }

        try {
            seconds = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            sender.sendMessage("Error: " + args[0] + " is not a number");
            return false;
        }

        if (args.length == 2 && !args[1].toLowerCase().matches("^true$|^false$")) {
            sender.sendMessage("Error: Argument for 'skip reset' must be 'true' or 'false'");
            return false;
        }

        if (args.length == 1 || args[1].equalsIgnoreCase("false")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), MinecraftManhuntPlugin.RESET_COMMAND_ALIAS);
        }

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), MinecraftManhuntPlugin.COUNTDOWN_COMMAND_ALIAS +
                " " + seconds);

        this.manhuntPlugin.setGameInProgress(true);
        return true;
    }
}
