package com.github.ricetech.minecraftmanhuntplugin.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@SuppressWarnings("unused")
public class NightVisionListener implements Listener {
    private final JavaPlugin plugin;

    public NightVisionListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void applyNightVision(Player player) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false)), 1);
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
