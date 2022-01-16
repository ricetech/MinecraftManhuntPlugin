package com.github.ricetech.minecraftmanhuntplugin.commands.internal;

import com.github.ricetech.minecraftmanhuntplugin.MinecraftManhuntPlugin;
import com.github.ricetech.minecraftmanhuntplugin.data.ManhuntTeam;
import com.github.ricetech.minecraftmanhuntplugin.data.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class SelfEliminateCommand implements CommandExecutor {
    private static final Map<String, Boolean> eligibility = new HashMap<>();

    public SelfEliminateCommand() {
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
        if (args.length != 0) {
            return false;
        }

        if (!(sender instanceof Player p)) {
            MinecraftManhuntPlugin.sendOnlyPlayersErrorMsg(sender);
            return true;
        }

        ManhuntTeam team = TeamManager.getTeam(p);

        if (team == null) {
            MinecraftManhuntPlugin.sendErrorMsg(p, "You aren't on a team. Use /" + MinecraftManhuntPlugin.TEAM_SWITCH_COMMAND_ALIAS + " to join a team.");
            return true;
        }

        if (!eligibility.getOrDefault(p.getName(), false)) {
            MinecraftManhuntPlugin.sendErrorMsg(p, "You are not eligible for elimination.");
            return true;
        }

        eligibility.put(p.getName(), false);
        // Disable eligibility for /teamtp
        TeamTpCommand.setEligibility(p.getName(), false);

        Bukkit.broadcastMessage(MinecraftManhuntPlugin.getBukkitTeamColor(TeamManager.getTeam(p)) + p.getName() +
                MinecraftManhuntPlugin.GAME_MSG_COLOR + "'s death was confirmed to be player-caused and as such, " +
                "will not be able to teleport.");

        if (team == ManhuntTeam.RUNNERS) {
            TeamManager.eliminatePlayer(p);
        }

        return true;
    }
}
