package com.github.ricetech.minecraftmanhuntplugin.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class TrackCommand implements CommandExecutor {
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

    public static void track(Player source, Player target) {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage("Error: Command not implemented");
        return true;
    }
}
