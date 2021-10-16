package com.github.ricetech.minecraftmanhuntplugin;

import com.github.ricetech.minecraftmanhuntplugin.commands.game.ClearTeamsCommand;
import com.github.ricetech.minecraftmanhuntplugin.commands.game.MakeTeamsCommand;
import com.github.ricetech.minecraftmanhuntplugin.commands.game.StartGameCommand;
import com.github.ricetech.minecraftmanhuntplugin.data.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ManhuntTabCompleter implements TabCompleter {
    @SuppressWarnings("DuplicateBranchesInSwitch")
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<List<String>> tabComplete;

        // The list of online players needs to be regenerated every time the function is called to stay up-to-date
        List<String> players = Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();

        switch (alias) {
            // Game
            case MinecraftManhuntPlugin.CLEAR_TEAMS_COMMAND_ALIAS -> tabComplete = null;
            case MinecraftManhuntPlugin.LIST_TEAMS_COMMAND_ALIAS -> tabComplete = null;
            case MinecraftManhuntPlugin.MAKE_TEAMS_COMMAND_ALIAS -> tabComplete = List.of(Arrays.asList("random", "select"));
            case MinecraftManhuntPlugin.START_GAME_COMMAND_ALIAS -> tabComplete = Arrays.asList(Collections.emptyList(), Arrays.asList("true", "false"));
            case MinecraftManhuntPlugin.STOP_GAME_COMMAND_ALIAS -> tabComplete = null;
            // Internal
            case MinecraftManhuntPlugin.RESET_ELIGIBILITY_COMMAND_ALIAS -> tabComplete = null;
            case MinecraftManhuntPlugin.SELF_ELIMINATE_COMMAND_ALIAS -> tabComplete = null;
            case MinecraftManhuntPlugin.TEAM_TP_COMMAND_ALIAS -> tabComplete = List.of(players);
            case MinecraftManhuntPlugin.TP_OPTIONS_COMMAND_ALIAS -> tabComplete = null;
            // OP
            case MinecraftManhuntPlugin.CREDIT_KILL_COMMAND_ALIAS -> tabComplete = List.of(players);
            case MinecraftManhuntPlugin.ELIMINATE_COMMAND_ALIAS -> tabComplete = Arrays.asList(players, players);
            case MinecraftManhuntPlugin.OFFER_TEAM_TP_COMMAND_ALIAS -> tabComplete = List.of(players);
            case MinecraftManhuntPlugin.REMOVE_DEATH_COMMAND_ALIAS -> tabComplete = List.of(players);
            case MinecraftManhuntPlugin.RESTORE_INVENTORY_COMMAND_ALIAS -> tabComplete = List.of(players);
            case MinecraftManhuntPlugin.SET_PLAYER_TEAM_COMMAND_ALIAS -> tabComplete = Arrays.asList(players, TeamManager.getValidTeams().stream().toList());
            case MinecraftManhuntPlugin.UN_ELIMINATE_COMMAND_ALIAS -> tabComplete = List.of(players);
            // Player
            case MinecraftManhuntPlugin.TEAM_SWITCH_COMMAND_ALIAS -> tabComplete = List.of(TeamManager.getValidTeams().stream().toList());
            case MinecraftManhuntPlugin.TRACK_COMMAND_ALIAS -> tabComplete = List.of(players);
            case MinecraftManhuntPlugin.TRACK_PORTAL_COMMAND_ALIAS -> tabComplete = null;
            // Util
            case MinecraftManhuntPlugin.COUNTDOWN_COMMAND_ALIAS -> tabComplete = null;
            case MinecraftManhuntPlugin.RESET_COMMAND_ALIAS -> tabComplete = null;
            case MinecraftManhuntPlugin.VERSION_COMMAND_ALIAS -> tabComplete = null;
            default -> tabComplete = null;
        }

        if (tabComplete == null) {
            return Collections.emptyList();
        }

        String partialArg = args.length > 0 ? args[args.length - 1] : "";
        int argNum = args.length > 0 ? args.length - 1 : 0;

        // Catch if user tries to autocomplete a command with no arguments or if they autocomplete more arguments
        if (argNum >= tabComplete.size()) {
            return Collections.emptyList();
        }

        return new ArrayList<>(tabComplete.get(argNum)).stream()
                .filter(option -> (partialArg.isEmpty() || option.toLowerCase().startsWith(partialArg.toLowerCase())))
                .collect(Collectors.toList());
    }
}
