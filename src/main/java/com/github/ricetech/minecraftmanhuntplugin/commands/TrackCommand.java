package com.github.ricetech.minecraftmanhuntplugin.commands;

import com.github.ricetech.minecraftmanhuntplugin.MinecraftManhuntPlugin;
import com.github.ricetech.minecraftmanhuntplugin.data.ManhuntTeam;
import com.github.ricetech.minecraftmanhuntplugin.data.TeamManager;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

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

    private static void sendTrackMsg(@NotNull Player source, @NotNull Location sourceLoc, @NotNull String targetName, @NotNull Location targetLoc) {
        long distance = 0;
        try {
            distance = Math.round(sourceLoc.distance(targetLoc));
        } catch (IllegalArgumentException e) {
            MinecraftManhuntPlugin.sendErrorMsg(source, "Locations are in different worlds. Please contact the developer.");
        }

        int sourceY = sourceLoc.getBlockY();
        int targetY = targetLoc.getBlockY();

        ManhuntTeam sourceTeam = TeamManager.getTeam(source);
        ManhuntTeam targetTeam = TeamManager.getTeam(targetName);

        ChatColor targetColor;
        if (targetTeam != null) {
            targetColor = MinecraftManhuntPlugin.getBukkitTeamColor(targetTeam);
        } else {
            targetColor = ChatColor.RESET;
        }

        if (sourceTeam == targetTeam ||
                (sourceTeam == ManhuntTeam.RUNNERS && targetTeam == ManhuntTeam.ELIMINATED) ||
                (sourceTeam == ManhuntTeam.ELIMINATED && targetTeam == ManhuntTeam.RUNNERS)) {
            // Same team, allow precise tracking
            source.sendMessage("Tracking " + targetColor + targetName + ChatColor.RESET + " at " +
                    "(" + targetLoc.getBlockX() + ", " + targetY + ", " + targetLoc.getBlockZ() + "), " +
                    distance + " blocks away from you.");
        } else {
            String distanceString;
            if (distance > 1000) {
                // Floor round to nearest 1000
                long distanceRounded = Math.round(Math.floor((double) distance / 1000) * 1000);
                distanceString = "over " + distanceRounded + " blocks away from you.";
            } else if (distance > 500) {
                distanceString = "over 500 blocks away from you";
            } else if (distance > 250) {
                distanceString = "over 250 blocks away from you";
            } else if (distance > 100) {
                distanceString = "over 100 blocks away from you";
            } else if (distance > 50) {
                distanceString = "over 50 blocks away from you";
            } else {
                distanceString = "less than 50 blocks away from you";
            }

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
            source.sendMessage("Tracking " + targetColor + targetName + ChatColor.RESET + ": The target is " +
                    distanceString + " and " + heightDiffString + ".");
        }
    }

    /**
     * @author @johnzhoudev
     * @author @ricetech
     */
    public static void updateCompass(Player p, @NotNull Location targetLoc) {
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

    public static void handleCompassRightClick(Player p) {
        String trackedPlayer = trackingMap.getOrDefault(p.getName(), null);

        if (trackedPlayer == null) {
            MinecraftManhuntPlugin.sendErrorMsg(p, "You aren't tracking anyone. Use /" + MinecraftManhuntPlugin.TRACK_COMMAND_ALIAS + "to start tracking.");
            return;
        }

        trackPlayer(p, trackedPlayer);
    }

    public static void trackPlayer(Player source, @NotNull String targetName) {
        // Check if target exists
        Player target = Bukkit.getPlayer(targetName);

        Location targetLoc;

        if (target != null) {
            // Player is online
            targetLoc = target.getLocation();
        } else {
            // Player is offline, check offlinePlayerLocation storage
            targetLoc = offlinePlayerLocations.getOrDefault(targetName, null);
            if (targetLoc == null) {
                MinecraftManhuntPlugin.sendErrorMsg(source, "Target player does not exist.");
                return;
            }
        }

        // Check teams
        ManhuntTeam sourceTeam = TeamManager.getTeam(source.getName());
        ManhuntTeam targetTeam = TeamManager.getTeam(targetName);

        if (sourceTeam == null) {
            MinecraftManhuntPlugin.sendErrorMsg(source, "You cannot track players because you are not on a team.");
            return;
        }

        if (targetTeam == null) {
            MinecraftManhuntPlugin.sendErrorMsg(source, "The target must be part of a team in order to be tracked.");
            return;
        }

        // Runners can only track other runners/eliminated team members
        if (sourceTeam != ManhuntTeam.HUNTERS && targetTeam != ManhuntTeam.RUNNERS && targetTeam != ManhuntTeam.ELIMINATED) {
            MinecraftManhuntPlugin.sendErrorMsg(source, "You can only track players who are Runners or Eliminated.");
        }

        // Prohibit tracking of Spectators
        if (targetTeam == ManhuntTeam.SPECTATORS) {
            MinecraftManhuntPlugin.sendErrorMsg(source, "You cannot track spectators.");
        }

        Location sourceLoc = source.getLocation();
        World targetWorld = targetLoc.getWorld();

        if (targetWorld == null) {
            MinecraftManhuntPlugin.sendErrorMsg(source, "The target is in an invalid world. Please contact the developer.");
            return;
        }

        World.Environment sourceWorldEnv = source.getWorld().getEnvironment();
        World.Environment targetWorldEnv = targetLoc.getWorld().getEnvironment();

        if (sourceWorldEnv == World.Environment.CUSTOM || targetWorldEnv == World.Environment.CUSTOM) {
            MinecraftManhuntPlugin.sendErrorMsg(source, "Tracking is not supported in Custom Worlds. Please contact the developer.");
        } else if (sourceWorldEnv == targetWorldEnv) {
            // Same world, track normally
            sendTrackMsg(source, sourceLoc, targetName, targetLoc);
            updateCompass(source, targetLoc);
        } else if ((sourceWorldEnv == World.Environment.NETHER && targetWorldEnv == World.Environment.THE_END) ||
                (sourceWorldEnv == World.Environment.THE_END && targetWorldEnv == World.Environment.NETHER)) {
            // Not the same world, not overworld. Track the source player's own exit portal.
            Location sourceExitPortal = portalExits.getOrDefault(source.getName(), null);
            if (sourceExitPortal == null) {
                MinecraftManhuntPlugin.sendErrorMsg(source, "The location of your exit portal is invalid and cannot be tracked.");
            } else {
                sendTrackMsg(source, sourceLoc, targetName, sourceExitPortal);
            }
        } else if (sourceWorldEnv != World.Environment.NORMAL && targetWorldEnv == World.Environment.NORMAL) {
            // Target in overworld, source not in overworld.
            // Priority 1: Track target's exit portal
            Location targetExitPortal = portalExits.getOrDefault(targetName, null);
            if (targetExitPortal != null) {
                World targetExitPortalWorld = targetExitPortal.getWorld();
                if (targetExitPortalWorld == null) {
                    MinecraftManhuntPlugin.sendErrorMsg(source, "World type is invalid. Please contact the developer.");
                    return;
                }

                World.Environment targetExitPortalEnv = targetExitPortalWorld.getEnvironment();
                // Since portalExits stores both Nether and End portals, need to check for same world first
                if (targetExitPortalEnv == sourceWorldEnv) {
                    sendTrackMsg(source, sourceLoc, targetName, targetExitPortal);
                    return;
                }
            }
            // Target exit portal is null
            // Priority 2: Track source's exit portal
            Location sourceExitPortal = portalExits.getOrDefault(source.getName(), null);
            if (sourceExitPortal == null) {
                MinecraftManhuntPlugin.sendErrorMsg(source, "The location of your exit portal is invalid and cannot be tracked.");
            } else {
                sendTrackMsg(source, sourceLoc, targetName, sourceExitPortal);
            }
        } else {
            // Source is in the overworld, target in Nether/The End. Track the target's entry portal.
            Location targetEntryPortal = portalEntrances.getOrDefault(targetName, null);

            if (targetEntryPortal == null) {
                MinecraftManhuntPlugin.sendErrorMsg(source, "The target entry portal is invalid and cannot be tracked.");
                return;
            }

            // Ensure environments match
            World targetEntryPortalWorld = targetEntryPortal.getWorld();
            if (targetEntryPortalWorld == null) {
                MinecraftManhuntPlugin.sendErrorMsg(source, "The target portal entrance is invalid. Please contact the developer.");
                return;
            }
            if (targetEntryPortalWorld.getEnvironment() != sourceWorldEnv) {
                MinecraftManhuntPlugin.sendErrorMsg(source, "The target portal entrance is not in your world. Please contact the developer.");
                return;
            }

            sendTrackMsg(source, sourceLoc, targetName, targetEntryPortal);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
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

        // Check if target is online or, if offline, their last location is cached
        if (target == null || offlinePlayerLocations.getOrDefault(args[0], null) != null) {
            trackPlayer(p, args[0]);
            trackingMap.put(p.getName(), args[0]);
            return true;
        } else {
            MinecraftManhuntPlugin.sendErrorMsg(p, "Target player does not exist or is offline.");
        }

        return true;
    }
}
