package com.github.ricetech.minecraftmanhuntplugin.listeners;

import com.github.ricetech.minecraftmanhuntplugin.commands.op.RestoreInventoryCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerInventoryStorageListener implements Listener {
    @EventHandler
    public void storeInventoryOnDeath(PlayerDeathEvent event) {
        Player p = event.getEntity();
        RestoreInventoryCommand.putInventory(p.getName(), p.getInventory().getContents());
    }
}
