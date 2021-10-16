package com.github.ricetech.minecraftmanhuntplugin.commands.game;

import com.github.ricetech.minecraftmanhuntplugin.MinecraftManhuntPlugin;
import com.github.ricetech.minecraftmanhuntplugin.commands.utility.ResetCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StartGameCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        int seconds;
        boolean reset = true;
        boolean freezeHunters = true;

        if (args.length < 1 || args.length > 3) {
            return false;
        }

        if (MinecraftManhuntPlugin.isGameInProgress) {
            MinecraftManhuntPlugin.sendErrorMsg(sender, "A game is already in progress.");
            return true;
        }

        try {
            seconds = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            MinecraftManhuntPlugin.sendErrorMsg(sender, args[0] + " is not a number");
            return false;
        }

        // Handle Reset arg
        if (args.length > 1) {
            if (!args[1].toLowerCase().matches("^true$|^false$")) {
                MinecraftManhuntPlugin.sendErrorMsg(sender, "Argument for 'don't reset' must be 'true' or 'false'");
                return false;
            } else {
                reset = !Boolean.parseBoolean(args[1]);
            }
        }

        if (args.length > 2) {
            if (!args[2].toLowerCase().matches("^true$|^false$")) {
                MinecraftManhuntPlugin.sendErrorMsg(sender, "Argument for 'don't freeze/reset Hunters' must be 'true' or 'false'");
                return false;
            } else {
                freezeHunters = !Boolean.parseBoolean(args[2]);
            }
        }

        if (reset) {
            ResetCommand.runReset();
        }

        ListTeamsCommand.listTeams(false, null);

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendTitle(MinecraftManhuntPlugin.GAME_MSG_COLOR + "Manhunt is starting!", "Get ready!",
                    MinecraftManhuntPlugin.TITLE_FADE_IN, MinecraftManhuntPlugin.TITLE_STAY, MinecraftManhuntPlugin.TITLE_FADE_OUT);
        }

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), MinecraftManhuntPlugin.COUNTDOWN_COMMAND_ALIAS +
                " " + seconds + " " + freezeHunters);

        MinecraftManhuntPlugin.isTeamSelectInProgress = false;
        MinecraftManhuntPlugin.isGameInProgress = true;
        return true;
    }
}
