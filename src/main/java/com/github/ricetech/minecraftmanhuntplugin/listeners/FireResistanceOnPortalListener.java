package com.github.ricetech.minecraftmanhuntplugin.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FireResistanceOnPortalListener implements Listener {
    @EventHandler
    public void giveFireResistance(PlayerPortalEvent event) {
        event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 300, 1));
    }
}
