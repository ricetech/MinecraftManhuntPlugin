package com.github.ricetech.minecraftmanhuntplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class CountdownCommand implements CommandExecutor {
    private final JavaPlugin plugin;

    public CountdownCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length != 1) {
            return false;
        }

        int seconds = Integer.parseInt(args[0]);
        Bukkit.broadcastMessage("Started countdown for " + seconds + " seconds");

        return true;
    }
}
