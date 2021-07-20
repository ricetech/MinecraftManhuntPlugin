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
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TeamSwitchCommand implements CommandExecutor {
    private static final Map<String, Boolean> eligibility = new HashMap<>();

    private final MinecraftManhuntPlugin manhuntPlugin;
    private final TeamManager teamManager;

    private final Set<String> validTeams;

    public TeamSwitchCommand(MinecraftManhuntPlugin manhuntPlugin) {
        this.manhuntPlugin = manhuntPlugin;
        this.teamManager = manhuntPlugin.getTeamManager();
        this.validTeams = teamManager.getValidTeams();
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

    public static void sendTeamSelectMsg(Player p) {
        ComponentBuilder builderSelectTeamMsg = new ComponentBuilder("Select a team:");

        TextComponent runnersComponent = new TextComponent("[RUNNERS]");
        runnersComponent.setColor(ChatColor.DARK_BLUE);
        runnersComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + MinecraftManhuntPlugin.TEAM_SWITCH_COMMAND_ALIAS + " runners"));

        TextComponent huntersComponent = new TextComponent("[HUNTERS]");
        huntersComponent.setColor(ChatColor.RED);
        huntersComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + MinecraftManhuntPlugin.TEAM_SWITCH_COMMAND_ALIAS + " hunters"));

        TextComponent spectatorsComponent = new TextComponent("[SPECTATORS]");
        spectatorsComponent.setColor(ChatColor.GOLD);
        spectatorsComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + MinecraftManhuntPlugin.TEAM_SWITCH_COMMAND_ALIAS + " spectators"));

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
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        Player p;

        if (args.length != 1) {
            return false;
        }

        if (!(sender instanceof Player)) {
            MinecraftManhuntPlugin.sendOnlyPlayersErrorMsg(sender);
            return true;
        } else {
            p = ((Player) sender);
        }

        if (!validTeams.contains(args[0].toUpperCase())) {
            MinecraftManhuntPlugin.sendErrorMsg(sender, args[0] + " is not a valid team.");
            return false;
        }

        if (manhuntPlugin.isGameInProgress()) {
            MinecraftManhuntPlugin.sendErrorMsg(sender, "You cannot change teams after the game has started.");
            return true;
        }

        if (!eligibility.getOrDefault(p.getName(), false)) {
            MinecraftManhuntPlugin.sendErrorMsg(sender, "You are not eligible for team changing.");
            return true;
        }

        eligibility.put(p.getName(), false);
        teamManager.editTeam(p, ManhuntTeam.valueOf(args[0].toUpperCase()));

        return true;
    }
}
