package com.github.ricetech.minecraftmanhuntplugin;

import org.bukkit.plugin.java.JavaPlugin;

public class MinecraftManhuntPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("Manhunt Plugin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Manhunt Plugin disabled!");
    }
}
