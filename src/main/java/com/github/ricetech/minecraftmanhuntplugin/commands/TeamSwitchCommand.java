package com.github.ricetech.minecraftmanhuntplugin.commands;

import com.github.ricetech.minecraftmanhuntplugin.data.ManhuntTeams;
import com.github.ricetech.minecraftmanhuntplugin.data.TeamManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class TeamSwitchCommand implements CommandExecutor {
    private TeamManager teamManager;
    private Set<String> validTeams = new HashSet<>();

    public TeamSwitchCommand(TeamManager teamManager) {
        this.teamManager = teamManager;
        for (ManhuntTeams team : ManhuntTeams.values()) {
            validTeams.add(team.name());
        }
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (args.length != 1) {
            return false;
        }

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("Error: Only players can use this command.");
            return false;
        }

        if (!validTeams.contains(args[0])) {
            commandSender.sendMessage("Error: " + args[0] + " is not a valid team.");
            return false;
        }

        // Cast commandSender to player, type safety verified above
        Player p = (Player) commandSender;

        teamManager.editTeam(p, ManhuntTeams.valueOf(args[0].toUpperCase()));

        return true;
    }
}
