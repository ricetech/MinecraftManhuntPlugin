package com.github.ricetech.minecraftmanhuntplugin.commands;

import com.github.ricetech.minecraftmanhuntplugin.MinecraftManhuntPlugin;
import com.github.ricetech.minecraftmanhuntplugin.data.ManhuntTeam;
import com.github.ricetech.minecraftmanhuntplugin.data.TeamManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TeamSwitchCommand implements CommandExecutor {
    private static final Map<String, Boolean> eligibility = new HashMap<>();

    private final TeamManager teamManager;

    private final Set<String> validTeams;

    public TeamSwitchCommand(MinecraftManhuntPlugin manhuntPlugin) {
        this.teamManager = manhuntPlugin.getTeamManager();
        this.validTeams = teamManager.getValidTeams();
    }

    public static boolean getEligibility(String entry) {
        return eligibility.getOrDefault(entry, false);
    }

    public static void setEligibility(String entry, boolean isEligible) {
        eligibility.put(entry, isEligible);
    }

    public static void sendTeamSelectMsg(Player p) {
        ComponentBuilder builderSelectTeamMsg = new ComponentBuilder("Select a team:");

        TextComponent runnersComponent = new TextComponent("[RUNNERS]");
        runnersComponent.setColor(ChatColor.DARK_BLUE);
        runnersComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/changeteam runners"));

        TextComponent huntersComponent = new TextComponent("[HUNTERS]");
        huntersComponent.setColor(ChatColor.RED);
        huntersComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/changeteam hunters"));

        TextComponent spectatorsComponent = new TextComponent("[SPECTATORS]");
        spectatorsComponent.setColor(ChatColor.GOLD);
        spectatorsComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/changeteam spectators"));

        builderSelectTeamMsg
                .append(" ")
                .append(runnersComponent)
                .append(" ")
                .append(huntersComponent)
                .append(" ")
                .append(spectatorsComponent);

        eligibility.put(p.getName(), true);
        p.spigot().sendMessage(builderSelectTeamMsg.create());
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        Player p;

        if (args.length != 1) {
            return false;
        }

        if (!(commandSender instanceof Player)) {
            MinecraftManhuntPlugin.sendOnlyPlayersErrorMsg(commandSender);
            return true;
        } else {
            p = ((Player) commandSender);
        }

        if (!validTeams.contains(args[0].toUpperCase())) {
            commandSender.sendMessage("Error: " + args[0] + " is not a valid team.");
            return false;
        }

        if (!eligibility.getOrDefault(p.getName(), false)) {
            commandSender.sendMessage(org.bukkit.ChatColor.RED + "Error: You are not eligible for team changing.");
            return true;
        }

        eligibility.put(p.getName(), false);
        teamManager.editTeam(p, ManhuntTeam.valueOf(args[0].toUpperCase()));

        return true;
    }
}
