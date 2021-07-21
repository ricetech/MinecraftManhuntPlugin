package com.github.ricetech.minecraftmanhuntplugin.listeners;

import com.github.ricetech.minecraftmanhuntplugin.MinecraftManhuntPlugin;
import com.github.ricetech.minecraftmanhuntplugin.commands.player.TrackCommand;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

public class PlayerPortalLocationStorageListener implements Listener {
    private final MinecraftManhuntPlugin manhuntPlugin;

    public PlayerPortalLocationStorageListener(MinecraftManhuntPlugin manhuntPlugin) {
        this.manhuntPlugin = manhuntPlugin;
    }

    @EventHandler
    private void storePortalLocation(PlayerPortalEvent event) {
        if (manhuntPlugin.isGameInProgress()) {
            Player p = event.getPlayer();

            Location from = event.getFrom();
            Location to = event.getTo();

            if (from.getWorld() == null) {
                MinecraftManhuntPlugin.sendErrorMsg(p, "The world you are in is invalid. Please contact the developer.");
                return;
            }

            if (to == null) {
                MinecraftManhuntPlugin.sendErrorMsg(p, "The location you are teleporting to is invalid. Please contact the developer.");
                return;
            }

            World.Environment fromEnv = from.getWorld().getEnvironment();

            if (fromEnv == World.Environment.NORMAL) {
                TrackCommand.putPortalEntrance(p.getName(), from);
                // Store exit portal location so player can track their own portal
                TrackCommand.putPortalExit(p.getName(), to);
            } else {
                // Player is leaving the Nether/End, so remove their entrance portal location
                TrackCommand.clearPortalEntrance(p.getName());
                // Store new exit portal location so other players can follow
                TrackCommand.putPortalExit(p.getName(), from);
            }
        }
    }
}
