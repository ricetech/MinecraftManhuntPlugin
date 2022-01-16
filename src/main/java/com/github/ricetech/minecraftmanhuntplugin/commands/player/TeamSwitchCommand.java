package com.github.ricetech.minecraftmanhuntplugin.commands.player;

import com.github.ricetech.minecraftmanhuntplugin.MinecraftManhuntPlugin;
import com.github.ricetech.minecraftmanhuntplugin.data.ManhuntTeam;
import com.github.ricetech.minecraftmanhuntplugin.data.TeamManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class TeamSwitchCommand implements CommandExecutor {
    private static final Map<String, Boolean> eligibility = new HashMap<>();

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
        ComponentBuilder builderSelectTeamMsg = new ComponentBuilder("Click a team to join it:");

        TextComponent runnersComponent = new TextComponent("[RUNNERS]");
        runnersComponent.setColor(MinecraftManhuntPlugin.RUNNERS_COLOR_BUNGEE);
        runnersComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + MinecraftManhuntPlugin.TEAM_SWITCH_COMMAND_ALIAS + " runners"));
        runnersComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to join the" + MinecraftManhuntPlugin.RUNNERS_COLOR + " RUNNERS " + ChatColor.RESET + "team")));

        TextComponent huntersComponent = new TextComponent("[HUNTERS]");
        huntersComponent.setColor(MinecraftManhuntPlugin.HUNTERS_COLOR_BUNGEE);
        huntersComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + MinecraftManhuntPlugin.TEAM_SWITCH_COMMAND_ALIAS + " hunters"));
        huntersComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to join the" + MinecraftManhuntPlugin.HUNTERS_COLOR + " HUNTERS " + ChatColor.RESET + "team")));

        TextComponent spectatorsComponent = new TextComponent("[SPECTATORS]");
        spectatorsComponent.setColor(MinecraftManhuntPlugin.SPECTATORS_COLOR_BUNGEE);
        spectatorsComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + MinecraftManhuntPlugin.TEAM_SWITCH_COMMAND_ALIAS + " spectators"));
        spectatorsComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to join the" + MinecraftManhuntPlugin.SPECTATORS_COLOR + " SPECTATORS " + ChatColor.RESET + "team")));

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
        if (args.length != 1) {
            return false;
        }

        if (!(sender instanceof Player p)) {
            MinecraftManhuntPlugin.sendOnlyPlayersErrorMsg(sender);
            return true;
        }

        ManhuntTeam currentTeam = TeamManager.getTeam(p);

        if (!TeamManager.getValidTeams().contains(args[0].toUpperCase())) {
            MinecraftManhuntPlugin.sendErrorMsg(sender, args[0] + " is not a valid team.");
            return false;
        }

        // Allow spectators to switch teams even if the game has started provided they are still eligible
        if (MinecraftManhuntPlugin.isGameInProgress && currentTeam != ManhuntTeam.SPECTATORS) {
            MinecraftManhuntPlugin.sendErrorMsg(sender, "You cannot change teams after the game has started.");
            eligibility.put(p.getName(), false);
            return true;
        }

        if (!eligibility.getOrDefault(p.getName(), false)) {
            MinecraftManhuntPlugin.sendErrorMsg(sender, "You are not eligible for team changing.");
            return true;
        }

        eligibility.put(p.getName(), false);
        TeamManager.editTeam(p, ManhuntTeam.valueOf(args[0].toUpperCase()));

        return true;
    }
}
