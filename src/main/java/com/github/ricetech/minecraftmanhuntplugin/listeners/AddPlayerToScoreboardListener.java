package com.github.ricetech.minecraftmanhuntplugin.listeners;

import com.github.ricetech.minecraftmanhuntplugin.data.ScoreKeeper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class AddPlayerToScoreboardListener implements Listener {
    @EventHandler
    public void addPlayerToScoreboard(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        ScoreKeeper.addNewPlayer(p);
    }
}
