package com.github.ricetech.minecraftmanhuntplugin.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;

@SuppressWarnings("unused")
public class InventoryHandlerListener implements Listener {
    public static void giveCompass(Player player) {
        PlayerInventory inventory = player.getInventory();

        // Do not add compass if player already has it
        if (inventory.contains(Material.COMPASS)) {
            return;
        }

        // Add compass to inventory
        inventory.addItem(new ItemStack(Material.COMPASS, 1));
    }

    @EventHandler
    public void compassOnJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        giveCompass(p);
    }

    @EventHandler
    public void compassOnRespawn(PlayerRespawnEvent event) {
        Player p = event.getPlayer();
        giveCompass(p);
    }

    @EventHandler
    public void removeCompassOnDeath(PlayerDeathEvent event) {
        List<ItemStack> drops = event.getDrops();
        for (int i = 0; i < drops.size(); i++) {
            ItemStack drop = drops.get(i);
            if (drop.getType() == Material.COMPASS) {
                drops.set(i, null);
            }
        }
    }
}
