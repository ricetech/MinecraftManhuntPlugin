package com.github.ricetech.minecraftmanhuntplugin.commands.game;

import com.github.ricetech.minecraftmanhuntplugin.MinecraftManhuntPlugin;
import com.github.ricetech.minecraftmanhuntplugin.data.ManhuntMilestone;
import com.github.ricetech.minecraftmanhuntplugin.listeners.GameMilestoneListener;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class SetObjectiveCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length != 1) {
            return false;
        }

        if (!MinecraftManhuntPlugin.milestoneAdvancements.containsValue(args[0])) {
            MinecraftManhuntPlugin.sendErrorMsg(sender, "Not a valid objective.\nValid arguments: " +
                    Arrays.toString(MinecraftManhuntPlugin.milestoneAdvancements.values().toArray()));
        }

        // Reverse lookup. Not ideal but there aren't too many values in the map... yet.
        ManhuntMilestone newObjective = MinecraftManhuntPlugin.milestoneAdvancements.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), args[0]))
                .map(Map.Entry::getKey)
                .toList()
                .getFirst(); // Map is 1-to-1

        MinecraftManhuntPlugin.manhuntObjective = newObjective;

        if (MinecraftManhuntPlugin.isGameInProgress && GameMilestoneListener.HAS_ANNOUNCEMENT_BEEN_PLAYED.getOrDefault(newObjective, false)) {
            // Game is already won
            StopGameCommand.stopGame(null, GameMilestoneListener.PREFIX_W + GameMilestoneListener.ANNOUNCEMENTS.get(MinecraftManhuntPlugin.manhuntObjective));
        } else {
            // Announce new objective
            Bukkit.broadcastMessage(MinecraftManhuntPlugin.GAME_MSG_COLOR + "Manhunt: New objective! The Runners must now win by " +
                    GameMilestoneListener.ANNOUNCEMENTS.getOrDefault(newObjective, "404 objective name not found") + "!");
        }

        return true;
    }
}
