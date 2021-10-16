package com.github.ricetech.minecraftmanhuntplugin.listeners;

import com.github.ricetech.minecraftmanhuntplugin.MinecraftManhuntPlugin;
import com.github.ricetech.minecraftmanhuntplugin.commands.player.TeamSwitchCommand;
import com.github.ricetech.minecraftmanhuntplugin.data.ManhuntTeam;
import com.github.ricetech.minecraftmanhuntplugin.data.TeamManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class SendTeamSelectOnJoin implements Listener {
    @EventHandler
    public void sendTeamSelectOnJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();

        if (MinecraftManhuntPlugin.isGameInProgress) {
            TeamManager.editTeam(p, ManhuntTeam.SPECTATORS);
            p.sendMessage(MinecraftManhuntPlugin.GAME_MSG_COLOR + "Welcome to Manhunt!");
            p.sendMessage(MinecraftManhuntPlugin.GAME_MSG_COLOR + "A game is in progress. With the permission of the other players, " +
                    "feel free to use the message below to join a team. You can also continue watching as a spectator " +
                    "without selecting a team.");
            TeamSwitchCommand.sendTeamSelectMsg(p);
        } else if (MinecraftManhuntPlugin.isTeamSelectInProgress) {
            p.sendMessage(MinecraftManhuntPlugin.GAME_MSG_COLOR + "Welcome to Manhunt!");
            p.sendMessage(MinecraftManhuntPlugin.GAME_MSG_COLOR + "Team Select is currently in progress. " +
                    "Please join a team using the message below.");
            TeamSwitchCommand.sendTeamSelectMsg(p);
        }

    }
}
