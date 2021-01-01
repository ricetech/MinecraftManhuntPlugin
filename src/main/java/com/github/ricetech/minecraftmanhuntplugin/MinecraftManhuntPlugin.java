package com.github.ricetech.minecraftmanhuntplugin;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class MinecraftManhuntPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("Manhunt Plugin enabled!");
        PluginManager manager = getServer().getPluginManager();
    }

    @Override
    public void onDisable() {
        getLogger().info("Manhunt Plugin disabled!");
    }
}
