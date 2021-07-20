package com.github.ricetech.minecraftmanhuntplugin.commands;

import com.github.ricetech.minecraftmanhuntplugin.MinecraftManhuntPlugin;
import com.github.ricetech.minecraftmanhuntplugin.data.ManhuntTeam;
import com.github.ricetech.minecraftmanhuntplugin.data.TeamManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class SelfEliminateCommand implements CommandExecutor {
    private static final Map<String, Boolean> eligibility = new HashMap<>();

    private final TeamManager teamManager;

    public SelfEliminateCommand(MinecraftManhuntPlugin manhuntPlugin) {
        this.teamManager = manhuntPlugin.getTeamManager();
    }

    public static boolean getEligibility(String entry) {
        return eligibility.getOrDefault(entry, false);
    }

    public static void setEligibility(String entry, boolean isEligible) {
        eligibility.put(entry, isEligible);
    }

    public static void reset() {
        eligibility.clear();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        Player p;

        if (!(sender instanceof Player)) {
            MinecraftManhuntPlugin.sendOnlyPlayersErrorMsg(sender);
            return true;
        } else {
            p = ((Player) sender);
        }

        ManhuntTeam team = teamManager.getTeam(p);

        if (team == null) {
            MinecraftManhuntPlugin.sendErrorMsg(p, "You aren't on a team. Use /"+ MinecraftManhuntPlugin.TEAM_SWITCH_COMMAND_ALIAS + " to join a team.");
            return true;
        }

        if (!eligibility.getOrDefault(p.getName(), false)) {
            MinecraftManhuntPlugin.sendErrorMsg(p, "You are not eligible for elimination.");
            return true;
        }

        if (team != ManhuntTeam.RUNNERS) {
            MinecraftManhuntPlugin.sendErrorMsg(p, "You cannot be eliminated unless you are a Runner.");
            return true;
        }

        eligibility.put(p.getName(), false);
        // Disable eligibility for /teamtp
        TeamTpCommand.setEligibility(p.getName(), false);

        teamManager.eliminatePlayer(p);
        return true;
    }
}
