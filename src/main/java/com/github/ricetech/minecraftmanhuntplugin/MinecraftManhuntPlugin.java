package com.github.ricetech.minecraftmanhuntplugin;

import com.github.ricetech.minecraftmanhuntplugin.commands.CountdownCommand;
import com.github.ricetech.minecraftmanhuntplugin.data.ScoreKeeper;
import com.github.ricetech.minecraftmanhuntplugin.listeners.InventoryHandlerListener;
import com.github.ricetech.minecraftmanhuntplugin.listeners.PlayerDeathCoordsListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class MinecraftManhuntPlugin extends JavaPlugin {
    private ScoreKeeper scoreKeeper;

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onEnable() {
        getLogger().info("Manhunt Plugin enabled!");

        // Init Scoreboard
        scoreKeeper = new ScoreKeeper();

        // Add Event Listeners
        PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new PlayerDeathCoordsListener(), this);
        manager.registerEvents(new InventoryHandlerListener(), this);

        // Add Commands
        this.getCommand("countdown").setExecutor(new CountdownCommand(this));
    }

    @Override
    public void onDisable() {
        getLogger().info("Manhunt Plugin disabled!");
    }
}
