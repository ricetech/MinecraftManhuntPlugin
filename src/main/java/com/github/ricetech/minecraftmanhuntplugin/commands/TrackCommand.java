package com.github.ricetech.minecraftmanhuntplugin.commands;

import com.github.ricetech.minecraftmanhuntplugin.MinecraftManhuntPlugin;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public class TrackCommand implements CommandExecutor {
    public static final int CLOSE_Y_THRESHOLD = 10;
    public static final int MEDIUM_Y_THRESHOLD = 25;
    public static final int FAR_Y_THRESHOLD = 50;

    private static final Map<String, String> trackingMap = new HashMap<>();
    private static final Map<String, Location> portalEntrances = new HashMap<>();
    private static final Map<String, Location> portalExits = new HashMap<>();
    private static final Map<String, Location> offlinePlayerLocations = new HashMap<>();

    public TrackCommand() {

    }

    public static void reset() {
        trackingMap.clear();
        portalEntrances.clear();
        portalExits.clear();
        offlinePlayerLocations.clear();
    }

    public static void putPortalEntrance(String playerName, Location location) {
        portalEntrances.put(playerName, location);
    }

    public static void clearPortalEntrance(String playerName) {
        portalEntrances.remove(playerName);
    }

    public static void putPortalExit(String playerName, Location location) {
        portalExits.put(playerName, location);
    }

    public static void clearPortalExit(String playerName) {
        portalExits.remove(playerName);
    }

    public static void putOfflinePlayerLocation(String playerName, Location location) {
        offlinePlayerLocations.put(playerName, location);
    }

    public static void clearOfflinePlayerLocation(String playerName) {
        offlinePlayerLocations.remove(playerName);
    }

    private static void sendTrackMsg(Player source, Location sourceLoc, String targetName, Location targetLoc) {
        int sourceY = sourceLoc.getBlockY();
        int targetY = targetLoc.getBlockY();

        int heightDiff = sourceY - targetY;
        String heightDiffString;

        if (heightDiff > -CLOSE_Y_THRESHOLD && heightDiff < CLOSE_Y_THRESHOLD) {
            heightDiffString = "around the same y-level as you";
        } else if (heightDiff < -CLOSE_Y_THRESHOLD && heightDiff > -MEDIUM_Y_THRESHOLD) {
            heightDiffString = "slightly below you";
        } else if (heightDiff < -MEDIUM_Y_THRESHOLD && heightDiff > -FAR_Y_THRESHOLD) {
            heightDiffString = "a good distance below you";
        } else if (heightDiff < -FAR_Y_THRESHOLD) {
            heightDiffString = "very far below you";
        } else if (heightDiff > CLOSE_Y_THRESHOLD && heightDiff < MEDIUM_Y_THRESHOLD) {
            heightDiffString = "slightly above you";
        } else if (heightDiff > MEDIUM_Y_THRESHOLD && heightDiff < FAR_Y_THRESHOLD) {
            heightDiffString = "a good distance above you";
        } else if (heightDiff > FAR_Y_THRESHOLD) {
            heightDiffString = "very far above you";
        } else {
            heightDiffString = "in an invalid state. Please contact the developer";
        }
        source.sendMessage(ChatColor.GREEN + "Tracking " + targetName + ": The target is " + heightDiffString + ".");
    }

    /**
     * @author @johnzhoudev
     * @author @ricetech
     */
    public static void updateCompass(Player p, Location targetLoc) {
        PlayerInventory inventory = p.getInventory();

        int compassPosition = inventory.first(Material.COMPASS);
        if (compassPosition == -1) {
            MinecraftManhuntPlugin.sendErrorMsg(p, "You used /track without a compass in your inventory.");
            return;
        }

        // Get compass metadata
        ItemStack compass = inventory.getItem(compassPosition);

        if (compass == null) {
            MinecraftManhuntPlugin.sendErrorMsg(p, "The compass in your inventory is invalid. Please contact the developer.");
            return;
        }

        ItemMeta compassMeta = compass.getItemMeta();

        if (compassMeta instanceof CompassMeta trackerCompassMeta) {
            trackerCompassMeta.setLodestoneTracked(false);
            trackerCompassMeta.setLodestone(targetLoc);

            compass.setItemMeta(trackerCompassMeta);
        }
    }

    public static void trackPlayer(Player source, Player target) {
        // Check if target exists
        if (target == null) {
            // TODO: Offline player location storage
            MinecraftManhuntPlugin.sendErrorMsg(source, "Target player does not exist.");
            return;
        }

        Location sourceLoc = source.getLocation();
        Location targetLoc = target.getLocation();

        World.Environment sourceWorldEnv = source.getWorld().getEnvironment();
        World.Environment targetWorldEnv = target.getWorld().getEnvironment();

        if (sourceWorldEnv == targetWorldEnv) {
            // Same world
            sendTrackMsg(source, sourceLoc, target.getName(), targetLoc);
            updateCompass(source, targetLoc);
        }
        // TODO: Handle different worlds
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p;

        sender.sendMessage("Error: Command not implemented");

        if (args.length != 1) {
            return false;
        }

        // Ensure sender is a player, then cast sender to Player
        if (!(sender instanceof Player)) {
            MinecraftManhuntPlugin.sendOnlyPlayersErrorMsg(sender);
            return true;
        } else {
            p = ((Player) sender);
        }

        // Attempt to get target player
        Player target = Bukkit.getPlayer(args[0]);

        // Check if target exists
        if (target == null) {
            // TODO: Offline player location storage
            MinecraftManhuntPlugin.sendErrorMsg(p, "Target player does not exist.");
            return true;
        } else {
            // Target exists and is online, track normally
            trackPlayer(p, target);
        }

        return true;
    }
}
