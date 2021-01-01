package com.github.ricetech.minecraftmanhuntplugin.commands;

import com.github.ricetech.minecraftmanhuntplugin.listeners.InventoryHandlerListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class ResetCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length > 0) {
            return false;
        }

        Location spawnLocation = Bukkit.getWorlds().get(0).getSpawnLocation();

        for (Player p : Bukkit.getOnlinePlayers()) {
            // Clear inventory
            p.getInventory().clear();

            // Give compass
            InventoryHandlerListener.giveCompass(p);

            // Teleport to spawn
            p.teleport(spawnLocation);
        }

        return false;
    }
}
