package com.github.ricetech.minecraftmanhuntplugin.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

@SuppressWarnings("unused")
public class PlayerDeathCoordsListener implements Listener {
    /**
     * If a player dies, tell them the world in which they died and the coordinates of their death through a
     * private message.
     */
    @EventHandler
    public void whisperDeathLocation(PlayerDeathEvent event) {
        Player p = event.getEntity();
        Location pLoc = p.getLocation();
        World world = p.getWorld();
        String worldEnv = world.getName();
        //noinspection EnhancedSwitchMigration
        switch (world.getEnvironment()) {
            case NORMAL:
                worldEnv = "the Overworld";
                break;
            case NETHER:
                worldEnv = "the Nether";
                break;
            case THE_END:
                worldEnv = "The End";
                break;
        }
        String deathCoords = " died at: " + pLoc.getBlockX() + ", " + pLoc.getBlockY() + ", " + pLoc.getBlockZ() +
                " in " + worldEnv;
        Bukkit.getLogger().info(p.getName() + deathCoords);
        p.sendMessage("You" + deathCoords);
    }
}
