package com.github.ricetech.minecraftmanhuntplugin.listeners;

import com.github.ricetech.minecraftmanhuntplugin.MinecraftManhuntPlugin;
import com.github.ricetech.minecraftmanhuntplugin.commands.TrackCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OfflinePlayerLocationListener implements Listener {
    private final MinecraftManhuntPlugin manhuntPlugin;

    public OfflinePlayerLocationListener(MinecraftManhuntPlugin manhuntPlugin) {
        this.manhuntPlugin = manhuntPlugin;
    }

    @EventHandler
    public void storePlayerDisconnectLocation(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        TrackCommand.putOfflinePlayerLocation(p.getName(), p.getLocation());
    }
}
