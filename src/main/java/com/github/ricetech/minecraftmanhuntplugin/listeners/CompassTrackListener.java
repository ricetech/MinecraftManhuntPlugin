package com.github.ricetech.minecraftmanhuntplugin.listeners;

import com.github.ricetech.minecraftmanhuntplugin.commands.player.TrackCommand;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class CompassTrackListener implements Listener {
    @EventHandler
    public void onCompassRightClick(PlayerInteractEvent event) {
        Player p = event.getPlayer();

        if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) &&
                ((p.getInventory().getItemInMainHand().getType() == Material.COMPASS && event.getHand() == EquipmentSlot.HAND) ||
                        (p.getInventory().getItemInOffHand().getType() == Material.COMPASS && event.getHand() == EquipmentSlot.OFF_HAND))) {
            TrackCommand.handleCompassRightClick(p);
        }
    }
}
