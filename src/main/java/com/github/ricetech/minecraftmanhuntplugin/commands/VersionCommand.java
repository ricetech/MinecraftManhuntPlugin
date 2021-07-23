package com.github.ricetech.minecraftmanhuntplugin.commands;

import com.github.ricetech.minecraftmanhuntplugin.MinecraftManhuntPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public record VersionCommand(MinecraftManhuntPlugin manhuntPlugin) implements CommandExecutor {
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length != 0) {
            return false;
        }

        String version = this.manhuntPlugin.getDescription().getVersion();

        sender.sendMessage(MinecraftManhuntPlugin.GAME_MSG_COLOR + "Minecraft Manhunt Plugin " + version);
        return true;
    }
}
