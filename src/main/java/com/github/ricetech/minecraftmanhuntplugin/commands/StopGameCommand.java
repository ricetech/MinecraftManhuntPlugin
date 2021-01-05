package com.github.ricetech.minecraftmanhuntplugin.commands;

import com.github.ricetech.minecraftmanhuntplugin.MinecraftManhuntPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StopGameCommand implements CommandExecutor {
    private MinecraftManhuntPlugin plugin;

    public StopGameCommand(MinecraftManhuntPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        this.plugin.setGameInProgress(false);
        return true;
    }
}
