package com.github.ricetech.minecraftmanhuntplugin.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@SuppressWarnings("unused")
public class NightVisionListener implements Listener {
    public static void applyNightVision(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false));
    }

    @EventHandler
    public void nvOnJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        applyNightVision(p);
    }

    @EventHandler
    public void nvOnRespawn(PlayerRespawnEvent event) {
        Player p = event.getPlayer();
        applyNightVision(p);
    }
}
