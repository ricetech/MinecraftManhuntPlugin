package com.github.ricetech.minecraftmanhuntplugin.commands;

import com.github.ricetech.minecraftmanhuntplugin.MinecraftManhuntPlugin;
import com.github.ricetech.minecraftmanhuntplugin.data.ManhuntTeam;
import com.github.ricetech.minecraftmanhuntplugin.data.TeamManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class TeamSwitchCommand implements CommandExecutor {
    private TeamManager teamManager;
    private Set<String> validTeams = new HashSet<>();

    public TeamSwitchCommand(TeamManager teamManager) {
        this.teamManager = teamManager;
        for (ManhuntTeam team : ManhuntTeam.values()) {
            validTeams.add(team.name());
        }
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (args.length != 1) {
            return false;
        }

        if (!(commandSender instanceof Player)) {
            MinecraftManhuntPlugin.sendOnlyPlayersErrorMsg(commandSender);
            return true;
        }

        if (!validTeams.contains(args[0].toUpperCase())) {
            commandSender.sendMessage("Error: " + args[0] + " is not a valid team.");
            return false;
        }

        // Cast commandSender to player, type safety verified above
        Player p = (Player) commandSender;

        teamManager.editTeam(p, ManhuntTeam.valueOf(args[0].toUpperCase()));

        return true;
    }
}
