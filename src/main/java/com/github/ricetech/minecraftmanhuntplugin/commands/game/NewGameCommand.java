package com.github.ricetech.minecraftmanhuntplugin.commands.game;

import com.github.ricetech.minecraftmanhuntplugin.MinecraftManhuntPlugin;
import com.github.ricetech.minecraftmanhuntplugin.commands.player.TeamSwitchCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class NewGameCommand implements CommandExecutor {
    private final MinecraftManhuntPlugin manhuntPlugin;

    public NewGameCommand(MinecraftManhuntPlugin manhuntPlugin) {
        this.manhuntPlugin = manhuntPlugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String labels, String[] args) {
        if (args.length != 0) {
            return false;
        }

        this.manhuntPlugin.setGameInProgress(false);
        Bukkit.broadcastMessage(ChatColor.AQUA + "Manhunt: A new game is starting. Please select your teams.");

        for (Player p : Bukkit.getOnlinePlayers()) {
            TeamSwitchCommand.sendTeamSelectMsg(p);
        }

        return true;
    }
}
