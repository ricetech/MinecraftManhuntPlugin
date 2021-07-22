package com.github.ricetech.minecraftmanhuntplugin.commands.game;

import com.github.ricetech.minecraftmanhuntplugin.MinecraftManhuntPlugin;
import com.github.ricetech.minecraftmanhuntplugin.data.TeamManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ClearTeamsCommand implements CommandExecutor {
    private final MinecraftManhuntPlugin manhuntPlugin;

    public ClearTeamsCommand(MinecraftManhuntPlugin manhuntPlugin) {
        this.manhuntPlugin = manhuntPlugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length != 0) {
            return false;
        }

        if (manhuntPlugin.isGameInProgress()) {
            MinecraftManhuntPlugin.sendErrorMsg(sender, "Cannot clear teams while game is in progress.");
            return true;
        }

        TeamManager.clearTeams();
        return true;
    }
}
