package com.github.ricetech.minecraftmanhuntplugin.commands.game;

import com.github.ricetech.minecraftmanhuntplugin.MinecraftManhuntPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class StopGameCommand implements CommandExecutor {
    private final MinecraftManhuntPlugin manhuntPlugin;

    public StopGameCommand(MinecraftManhuntPlugin manhuntPlugin) {
        this.manhuntPlugin = manhuntPlugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        this.manhuntPlugin.setGameInProgress(false);
        Bukkit.broadcastMessage(MinecraftManhuntPlugin.GAME_MSG_COLOR + "Manhunt: The game has ended.");
        return true;
    }
}
