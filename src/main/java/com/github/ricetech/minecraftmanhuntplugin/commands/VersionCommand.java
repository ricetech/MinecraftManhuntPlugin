package com.github.ricetech.minecraftmanhuntplugin.commands;

import com.github.ricetech.minecraftmanhuntplugin.MinecraftManhuntPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class VersionCommand implements CommandExecutor {
    private final MinecraftManhuntPlugin manhuntPlugin;

    public VersionCommand(MinecraftManhuntPlugin manhuntPlugin){
        this.manhuntPlugin = manhuntPlugin;
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length != 0) {
            return false;
        }

        String version = this.manhuntPlugin.getDescription().getVersion();

        sender.sendMessage(MinecraftManhuntPlugin.GAME_MSG_COLOR + "Minecraft Manhunt Plugin v" + version);
        return true;
    }
}
