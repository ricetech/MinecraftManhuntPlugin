package com.github.ricetech.minecraftmanhuntplugin.commands.op;

import com.github.ricetech.minecraftmanhuntplugin.MinecraftManhuntPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class RestoreInventoryCommand implements CommandExecutor {
    private final static Map<String, PlayerInventory> inventories = new HashMap<>();

    public static PlayerInventory getInventory(String entry) {
        return inventories.getOrDefault(entry, null);
    }

    public static void putInventory(String entry, PlayerInventory inventory) {
        inventories.put(entry, inventory);
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length != 1) {
            return false;
        }

        Player p = Bukkit.getPlayer(args[0]);

        if (p == null) {
            MinecraftManhuntPlugin.sendErrorMsg(sender, "Target player does not exist or is offline.");
            return true;
        }

        PlayerInventory inventory = inventories.getOrDefault(p.getName(), null);

        if (inventory == null) {
            MinecraftManhuntPlugin.sendErrorMsg(sender, "Target player does not have a stored inventory.");
            return true;
        }

        p.openInventory(inventory);

        return true;
    }
}
