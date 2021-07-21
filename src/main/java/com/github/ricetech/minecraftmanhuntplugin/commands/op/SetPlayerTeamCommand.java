package com.github.ricetech.minecraftmanhuntplugin.commands.op;

import com.github.ricetech.minecraftmanhuntplugin.MinecraftManhuntPlugin;
import com.github.ricetech.minecraftmanhuntplugin.data.ManhuntTeam;
import com.github.ricetech.minecraftmanhuntplugin.data.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetPlayerTeamCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length != 2) {
            return false;
        }

        Player p = Bukkit.getPlayer(args[0]);

        if (p == null) {
            MinecraftManhuntPlugin.sendErrorMsg(sender, "Target player does not exist or is offline.");
            return true;
        }

        String teamString = args[1];

        if (!TeamManager.getValidTeams().contains(teamString.toUpperCase())) {
            MinecraftManhuntPlugin.sendErrorMsg(sender, args[1] + " is not a valid team.");
        }

        TeamManager.editTeam(p, ManhuntTeam.valueOf(args[1].toUpperCase()));

        ManhuntTeam newTeam = TeamManager.getTeam(p);
        ChatColor newTeamColor = MinecraftManhuntPlugin.getBukkitTeamColor(newTeam);

        Bukkit.broadcastMessage(ChatColor.DARK_AQUA + p.getName() + " has been switched to the " + newTeamColor +
                newTeam.name().toLowerCase() + " team.");

        return true;
    }
}
