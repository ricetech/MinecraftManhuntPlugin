package com.github.ricetech.minecraftmanhuntplugin.commands.game;

import com.github.ricetech.minecraftmanhuntplugin.MinecraftManhuntPlugin;
import com.github.ricetech.minecraftmanhuntplugin.commands.player.TeamSwitchCommand;
import com.github.ricetech.minecraftmanhuntplugin.data.ManhuntTeam;
import com.github.ricetech.minecraftmanhuntplugin.data.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MakeTeamsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String labels, String[] args) {
        if (args.length != 1 && args.length != 0) {
            return false;
        }

        if (MinecraftManhuntPlugin.isGameInProgress) {
            MinecraftManhuntPlugin.sendErrorMsg(sender, "Cannot make teams while game is in progress.");
        }

        if (args.length == 0 || args[0].equalsIgnoreCase("SELECT")) {
            // Select own teams
            Bukkit.broadcastMessage(MinecraftManhuntPlugin.GAME_MSG_COLOR + "Manhunt: A new game is starting. Please select your teams.");

            for (Player p : Bukkit.getOnlinePlayers()) {
                TeamSwitchCommand.sendTeamSelectMsg(p);
            }
        } else if (args[0].equalsIgnoreCase("RANDOM")) {
            // Random teams
            List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
            Collections.shuffle(onlinePlayers);

            Bukkit.broadcastMessage(MinecraftManhuntPlugin.GAME_MSG_COLOR + "Manhunt: A new game is starting with randomized teams.");

            for (int i = 0; i < onlinePlayers.size(); i++) {
                // Give extra players to Hunters
                if (i < Math.round(Math.floor((double) onlinePlayers.size() / 2))) {
                    // Runners
                    TeamManager.editTeam(onlinePlayers.get(i), ManhuntTeam.RUNNERS);
                } else {
                    // Hunters
                    TeamManager.editTeam(onlinePlayers.get(i), ManhuntTeam.HUNTERS);
                }
            }
        } else {
            return false;
        }
        return true;
    }
}
