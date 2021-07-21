package com.github.ricetech.minecraftmanhuntplugin.commands.op;

import com.github.ricetech.minecraftmanhuntplugin.MinecraftManhuntPlugin;
import com.github.ricetech.minecraftmanhuntplugin.data.ManhuntTeam;
import com.github.ricetech.minecraftmanhuntplugin.data.ScoreKeeper;
import com.github.ricetech.minecraftmanhuntplugin.data.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class EliminatePlayerCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length != 2) {
            return false;
        }

        Player victim = Bukkit.getPlayer(args[0]);
        Player killer = Bukkit.getPlayer(args[1]);

        if (victim == null) {
            MinecraftManhuntPlugin.sendErrorMsg(sender, "Victim does not exist or is offline.");
            return true;
        }

        if (killer == null) {
            MinecraftManhuntPlugin.sendErrorMsg(sender, "Killer does not exist or is offline.");
            return true;
        }

        ManhuntTeam victimTeam = TeamManager.getTeam(victim);
        ManhuntTeam killerTeam = TeamManager.getTeam(killer);

        if (victimTeam == ManhuntTeam.ELIMINATED) {
            MinecraftManhuntPlugin.sendErrorMsg(sender, "Target player has already been eliminated.");
            return true;
        } else if (victimTeam != ManhuntTeam.RUNNERS) {
            MinecraftManhuntPlugin.sendErrorMsg(sender, "Only Runners can be eliminated.");
            return true;
        }

        if (killerTeam != ManhuntTeam.HUNTERS) {
            MinecraftManhuntPlugin.sendErrorMsg(sender, "Killer is not a Hunter.");
            return true;
        }

        TeamManager.eliminatePlayer(victim);
        ScoreKeeper.addKill(killer);

        // Kill the player to reset them to spawn, just in case they haven't already
        victim.setHealth(0.0D);

        killer.sendMessage(ChatColor.DARK_GRAY + "You have been credited with eliminating " + victim.getName() + ".");

        return true;
    }
}
