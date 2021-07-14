package com.github.ricetech.minecraftmanhuntplugin.listeners;

import com.github.ricetech.minecraftmanhuntplugin.MinecraftManhuntPlugin;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.HashMap;
import java.util.Map;

public class PlayerDeathLocationStorageListener implements Listener {
    private final MinecraftManhuntPlugin manhuntPlugin;
    private static Map<String, Location> deathLocations = new HashMap<>();

    public PlayerDeathLocationStorageListener(MinecraftManhuntPlugin manhuntPlugin) {
        this.manhuntPlugin = manhuntPlugin;
    }

    public static Location getLatestDeathLoc(Player p) {
        return deathLocations.getOrDefault(p.getName(), null);
    }

    public static void resetDeathLocations() {
        deathLocations = new HashMap<>();
    }

    @EventHandler
    private void recordDeathLocation(PlayerDeathEvent deathEvent) {
        if (manhuntPlugin.isGameInProgress()) {
            Player p = deathEvent.getEntity();
            Location pLoc = p.getLocation();
            deathLocations.put(p.getName(), pLoc);
        }
    }
}
