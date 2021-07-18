package com.github.ricetech.minecraftmanhuntplugin.commands;

import com.github.ricetech.minecraftmanhuntplugin.MinecraftManhuntPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class TrackCommand implements CommandExecutor {
    public static final int CLOSE_Y_THRESHOLD = 10;
    public static final int MEDIUM_Y_THRESHOLD = 25;
    public static final int FAR_Y_THRESHOLD = 50;

    private static Map<String, String> trackingMap = new HashMap<>();
    private static Map<String, Location> portalEntrances = new HashMap<>();
    private static Map<String, Location> portalExits = new HashMap<>();

    public TrackCommand() {

    }

    public static void reset() {
        trackingMap = new HashMap<>();
        portalEntrances = new HashMap<>();
        portalExits = new HashMap<>();
    }

    private static void sendTrackMsg(Player source, Location sourceLoc, String targetName, Location targetLoc) {
        // TODO: Update Compass

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
        }

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage("Error: Command not implemented");
        return true;
    }
}
