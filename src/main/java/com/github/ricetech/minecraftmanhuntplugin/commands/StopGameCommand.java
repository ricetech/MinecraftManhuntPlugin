package com.github.ricetech.minecraftmanhuntplugin.commands;

import com.github.ricetech.minecraftmanhuntplugin.MinecraftManhuntPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StopGameCommand implements CommandExecutor {
    private final MinecraftManhuntPlugin manhuntPlugin;

    public StopGameCommand(MinecraftManhuntPlugin manhuntPlugin) {
        this.manhuntPlugin = manhuntPlugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        this.manhuntPlugin.setGameInProgress(false);
        Bukkit.broadcastMessage(ChatColor.AQUA + "Manhunt: The game has ended.");
        return true;
    }
}
