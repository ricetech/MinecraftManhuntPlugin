package com.github.ricetech.minecraftmanhuntplugin.listeners;

import com.github.ricetech.minecraftmanhuntplugin.data.ScoreKeeper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {
    private ScoreKeeper scoreKeeper;

    public PlayerDeathListener(ScoreKeeper scoreKeeper) {
        this.scoreKeeper = scoreKeeper;
    }

    @EventHandler
    public void eliminationMsgOnDeath(PlayerDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        if (killer != null) {
            // Increment killer's kills
            this.scoreKeeper.addKill(killer);
            // No other actions - teleports are not allowed after a death to another player
        } else {
            // Ask if this death occurred because of a player or not
        }
    }
}
