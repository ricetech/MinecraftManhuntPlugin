package com.github.ricetech.minecraftmanhuntplugin.commands.game;

import com.github.ricetech.minecraftmanhuntplugin.MinecraftManhuntPlugin;
import com.github.ricetech.minecraftmanhuntplugin.data.ManhuntTeam;
import com.github.ricetech.minecraftmanhuntplugin.data.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("StringConcatenationInsideStringBufferAppend")
public class ListTeamsCommand implements CommandExecutor {
    public static void listTeams(boolean whisper, CommandSender sender) {
        boolean hasEliminated = false;
        boolean hasSpectators = false;

        StringBuilder runners = new StringBuilder("Runners:");
        StringBuilder hunters = new StringBuilder("Hunters:");
        StringBuilder eliminated = new StringBuilder("Eliminated:");
        StringBuilder spectators = new StringBuilder("Spectators:");

        for (String entry : TeamManager.getTeamEntries(ManhuntTeam.RUNNERS)) {
            runners.append(" ").append(MinecraftManhuntPlugin.getBukkitTeamColor(ManhuntTeam.RUNNERS) + entry);
        }

        for (String entry : TeamManager.getTeamEntries(ManhuntTeam.HUNTERS)) {
            hunters.append(" ").append(MinecraftManhuntPlugin.getBukkitTeamColor(ManhuntTeam.HUNTERS) + entry);
        }

        for (String entry : TeamManager.getTeamEntries(ManhuntTeam.ELIMINATED)) {
            eliminated.append(" ").append(MinecraftManhuntPlugin.getBukkitTeamColor(ManhuntTeam.ELIMINATED) + entry);
            hasEliminated = true;
        }

        for (String entry : TeamManager.getTeamEntries(ManhuntTeam.SPECTATORS)) {
            spectators.append(" ").append(MinecraftManhuntPlugin.getBukkitTeamColor(ManhuntTeam.SPECTATORS) + entry);
            hasSpectators = true;
        }

        if (whisper && sender != null) {
            sender.sendMessage("Teams for this game:");
            sender.sendMessage(runners.toString());
            sender.sendMessage(hunters.toString());
            if (hasEliminated) {
                sender.sendMessage(eliminated.toString());
            }
            if (hasSpectators) {
                sender.sendMessage(spectators.toString());
            }
        } else {
            Bukkit.broadcastMessage("Teams for this game:");
            Bukkit.broadcastMessage(runners.toString());
            Bukkit.broadcastMessage(hunters.toString());
            if (hasEliminated) {
                Bukkit.broadcastMessage(eliminated.toString());
            }
            if (hasSpectators) {
                Bukkit.broadcastMessage(spectators.toString());
            }
        }
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length != 0) {
            return false;
        }

        listTeams(true, sender);
        return true;
    }
}
