package com.github.ricetech.minecraftmanhuntplugin.commands.player;

import com.github.ricetech.minecraftmanhuntplugin.MinecraftManhuntPlugin;
import com.github.ricetech.minecraftmanhuntplugin.data.ManhuntTeam;
import com.github.ricetech.minecraftmanhuntplugin.data.TeamManager;
import com.github.ricetech.minecraftmanhuntplugin.listeners.CompassInventoryHandlerListener;
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
import java.util.logging.Level;

public class TrackCommand implements CommandExecutor {
    // Safe keyword since Minecraft usernames cannot contain spaces
    public static final String PORTAL_NAME_KEY = "your portal";

    public static final int CLOSE_Y_THRESHOLD = 10;
    public static final int MEDIUM_Y_THRESHOLD = 25;
    public static final int FAR_Y_THRESHOLD = 50;

    public static final int DISTANCE_THRESHOLD_1 = 50;
    public static final int DISTANCE_THRESHOLD_2 = 100;
    public static final int DISTANCE_THRESHOLD_3 = 250;
    public static final int DISTANCE_THRESHOLD_4 = 500;
    public static final int DISTANCE_THRESHOLD_5 = 1000;

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

    public static void putTrackingEntry(String playerName, String tracked) {
        trackingMap.put(playerName, tracked);
    }

    /**
     * @author @johnzhoudev
     * @author @ricetech
     */
    public static void updateCompass(Player p, @NotNull Location targetLoc) {
        PlayerInventory inventory = p.getInventory();

        int compassPosition = inventory.first(Material.COMPASS);
        if (compassPosition == -1) {
            CompassInventoryHandlerListener.giveCompass(p);
            MinecraftManhuntPlugin.sendErrorMsg(p, "You used /track without a compass in your inventory. " +
                    "We attempted to give you another compass - please try tracking again. " +
                    "If you see this error again, make sure that your inventory isn't full. " +
                    "Otherwise, please contact the developer.");
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

    private static void sendTrackUpdate(@NotNull Player source, @NotNull Location sourceLoc, @NotNull String targetName, @NotNull Location targetLoc) {
        long distance;
        try {
            distance = Math.round(sourceLoc.distance(targetLoc));
        } catch (IllegalArgumentException e) {
            MinecraftManhuntPlugin.sendErrorMsg(source, "Locations are in different worlds. Please contact the developer.");
            return;
        }

        int sourceY = sourceLoc.getBlockY();
        int targetY = targetLoc.getBlockY();

        int heightDiff = sourceY - targetY;

        ManhuntTeam sourceTeam = TeamManager.getTeam(source);
        ManhuntTeam targetTeam = TeamManager.getTeam(targetName);

        ChatColor targetColor = MinecraftManhuntPlugin.getBukkitTeamColor(targetTeam);

        // Update compass
        updateCompass(source, targetLoc);

        // Tracking for teammates and Spectators (Precise location)
        if (sourceTeam == targetTeam || sourceTeam == ManhuntTeam.SPECTATORS || targetName.equals(PORTAL_NAME_KEY) ||
                (sourceTeam == ManhuntTeam.RUNNERS && targetTeam == ManhuntTeam.ELIMINATED) ||
                (sourceTeam == ManhuntTeam.ELIMINATED && targetTeam == ManhuntTeam.RUNNERS)) {
            String heightDiffString;

            if (heightDiff > 0) {
                heightDiffString = "+";
            } else {
                heightDiffString = "";
            }

            source.sendMessage("Tracking " + targetColor + targetName + ChatColor.RESET + ".\n" +
                    "Coordinates: (" + targetLoc.getBlockX() + ", " + targetY + ", " + targetLoc.getBlockZ() + ").\n" +
                    "Horizontal Distance: " + distance + " blocks.\n" +
                    "Vertical Distance: " + heightDiffString + heightDiff + " blocks.");
            // Tracking for enemies (Approx location)
        } else {
            String distanceString;
            if (distance > DISTANCE_THRESHOLD_5) {
                // Floor round to nearest 1000
                long distanceRounded = Math.round(Math.floor((double) distance / DISTANCE_THRESHOLD_5) * DISTANCE_THRESHOLD_5);
                distanceString = distanceRounded + "+ blocks away";
            } else if (distance > DISTANCE_THRESHOLD_4) {
                distanceString = "500+ blocks away";
            } else if (distance > DISTANCE_THRESHOLD_3) {
                distanceString = "250+ blocks away";
//            } else if (distance > DISTANCE_THRESHOLD_2) {
//                distanceString = "100+ blocks away";
//            } else if (distance > DISTANCE_THRESHOLD_1) {
//                distanceString = "50+ blocks away";
            } else {
                distanceString = "Less than 250 blocks away";
            }

            String heightDiffString;

            if (heightDiff >= -CLOSE_Y_THRESHOLD && heightDiff <= CLOSE_Y_THRESHOLD) {
                heightDiffString = "Around the same y-level as you";
            } else if (heightDiff > CLOSE_Y_THRESHOLD && heightDiff <= MEDIUM_Y_THRESHOLD) {
                heightDiffString = "Slightly below you";
            } else if (heightDiff > MEDIUM_Y_THRESHOLD && heightDiff <= FAR_Y_THRESHOLD) {
                heightDiffString = "A good distance below you";
            } else if (heightDiff > FAR_Y_THRESHOLD) {
                heightDiffString = "Very far below you";
            } else if (heightDiff >= -MEDIUM_Y_THRESHOLD) {
                heightDiffString = "Slightly above you";
            } else if (heightDiff >= -FAR_Y_THRESHOLD) {
                heightDiffString = "A good distance above you";
            } else {
                heightDiffString = "Very far above you";
                Bukkit.getLogger().log(Level.INFO, "Else triggered, diff " + heightDiff);
            }
            source.sendMessage("Tracking " + targetColor + targetName + ".");
            source.sendMessage("Distance: " + distanceString);
            source.sendMessage("Height: " + heightDiffString);
        }
    }

    public static void handleCompassRightClick(Player p) {
        String trackedPlayer = trackingMap.getOrDefault(p.getName(), null);

        if (trackedPlayer == null) {
            MinecraftManhuntPlugin.sendErrorMsg(p, "You aren't tracking anyone. Use /" + MinecraftManhuntPlugin.TRACK_COMMAND_ALIAS + " to start tracking.");
            return;
        }

        if (trackedPlayer.equals(PORTAL_NAME_KEY)) {
            trackPortal(p);
        } else {
            trackPlayer(p, trackedPlayer);
        }
    }

    public static void trackPortal(Player p) {
        World.Environment playerEnv = p.getWorld().getEnvironment();

        if (playerEnv != World.Environment.NETHER) {
            MinecraftManhuntPlugin.sendErrorMsg(p, "You can only track portals in The Nether.");
            return;
        }

        Location portalLoc = portalExits.getOrDefault(p.getName(), null);

        if (portalLoc == null || portalLoc.getWorld() == null) {
            MinecraftManhuntPlugin.sendErrorMsg(p, "You do not have a valid portal to track. Try using a portal first.");
            return;
        }

        World.Environment portalEnv = portalLoc.getWorld().getEnvironment();

        if (portalEnv != playerEnv) {
            MinecraftManhuntPlugin.sendErrorMsg(p, "You are not in the same world as your portal.");
            return;
        }

        sendTrackUpdate(p, p.getLocation(), PORTAL_NAME_KEY, portalLoc);
    }

    public static void trackPlayer(Player source, @NotNull String targetName) {
        if (targetName.equals(source.getName())) {
            MinecraftManhuntPlugin.sendErrorMsg(source, "You can't track yourself. Select someone else.");
            return;
        }

        // Check if target exists
        Player target = Bukkit.getPlayerExact(targetName);

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
            source.sendMessage(MinecraftManhuntPlugin.WARNING_MSG_COLOR + "Warning: The target player is offline. Tracking using their last known location.");
        }

        // Check teams
        ManhuntTeam sourceTeam = TeamManager.getTeam(source.getName());
        ManhuntTeam targetTeam = TeamManager.getTeam(targetName);

        if (sourceTeam == null) {
            MinecraftManhuntPlugin.sendErrorMsg(source, "You cannot track players because you are not on a team.");
            TeamSwitchCommand.setEligibility(source.getName(), true);
            TeamSwitchCommand.sendTeamSelectMsg(source);
            return;
        }

        if (targetTeam == null) {
            MinecraftManhuntPlugin.sendErrorMsg(source, "The target must be part of a team in order to be tracked.");
            return;
        }

        // Runners can only track other runners/eliminated team members
        if (sourceTeam != ManhuntTeam.HUNTERS && sourceTeam != ManhuntTeam.SPECTATORS && targetTeam != ManhuntTeam.RUNNERS && targetTeam != ManhuntTeam.ELIMINATED) {
            MinecraftManhuntPlugin.sendErrorMsg(source, "You can only track your teammates (Runners or Eliminated).");
            return;
        }

        // Prohibit tracking of Spectators
        if (sourceTeam != ManhuntTeam.SPECTATORS && targetTeam == ManhuntTeam.SPECTATORS) {
            MinecraftManhuntPlugin.sendErrorMsg(source, "You cannot track Spectators.");
            return;
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
            sendTrackUpdate(source, sourceLoc, targetName, targetLoc);
        } else if ((sourceWorldEnv == World.Environment.NETHER && targetWorldEnv == World.Environment.THE_END) ||
                (sourceWorldEnv == World.Environment.THE_END && targetWorldEnv == World.Environment.NETHER)) {
            // Not the same world, not overworld. Track the source player's own exit portal.
            Location sourceExitPortal = portalExits.getOrDefault(source.getName(), null);
            if (sourceExitPortal == null) {
                MinecraftManhuntPlugin.sendErrorMsg(source, "The location of your exit portal is invalid and cannot be tracked.");
            } else {
                sendTrackUpdate(source, sourceLoc, targetName, sourceExitPortal);
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
                    sendTrackUpdate(source, sourceLoc, targetName, targetExitPortal);
                    return;
                }
            }
            // Target exit portal is null
            // Priority 2: Track source's exit portal
            Location sourceExitPortal = portalExits.getOrDefault(source.getName(), null);
            if (sourceExitPortal == null) {
                MinecraftManhuntPlugin.sendErrorMsg(source, "The location of your exit portal is invalid and cannot be tracked.");
            } else {
                source.sendMessage(MinecraftManhuntPlugin.WARNING_MSG_COLOR + "Warning: The target doesn't have a valid " +
                        "exit portal. Tracking your exit portal instead.");
                sendTrackUpdate(source, sourceLoc, targetName, sourceExitPortal);
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

            sendTrackUpdate(source, sourceLoc, targetName, targetEntryPortal);
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length != 1) {
            return false;
        }

        // Ensure sender is a player, then cast sender to Player
        if (!(sender instanceof Player p)) {
            MinecraftManhuntPlugin.sendOnlyPlayersErrorMsg(sender);
            return true;
        }

        // Attempt to get target player
        Player target = Bukkit.getPlayerExact(args[0]);

        // Check if target is online or, if offline, their last location is cached
        if (target != null || offlinePlayerLocations.getOrDefault(args[0], null) != null) {
            trackPlayer(p, args[0]);
            trackingMap.put(p.getName(), args[0]);
            return true;
        } else {
            MinecraftManhuntPlugin.sendErrorMsg(p, "Target player does not exist or is offline.");
        }

        return true;
    }
}
