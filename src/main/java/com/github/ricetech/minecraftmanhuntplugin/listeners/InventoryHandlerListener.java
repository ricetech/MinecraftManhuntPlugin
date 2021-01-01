package com.github.ricetech.minecraftmanhuntplugin.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class InventoryHandlerListener implements Listener {
    private void giveCompass(Player player) {
        PlayerInventory inventory = player.getInventory();

        // Do not add compass if player already has it
        if (inventory.contains(Material.COMPASS)) {
            return;
        }

        // Add compass to inventory
        inventory.addItem(new ItemStack(Material.COMPASS, 1));
    }

}
