package com.github.ricetech.minecraftmanhuntplugin.commands;

import com.github.ricetech.minecraftmanhuntplugin.data.ScoreKeeper;
import com.github.ricetech.minecraftmanhuntplugin.listeners.InventoryHandlerListener;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

/**
 * Reset the world and all players in preparation for starting a new game of Manhunt.
 */
@SuppressWarnings("unused")
public class ResetCommand implements CommandExecutor {
    private final ScoreKeeper scoreKeeper;

    public ResetCommand(ScoreKeeper scoreKeeper) {
        this.scoreKeeper = scoreKeeper;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length > 0) {
            return false;
        }

        World overworld = Bukkit.getServer().getWorlds().get(0);

        Location spawnLocation = overworld.getSpawnLocation();

        for (Player p : Bukkit.getOnlinePlayers()) {
            // Reset scores
            this.scoreKeeper.resetPlayer(p);

            // Clear inventory
            p.getInventory().clear();

            // Give compass
            InventoryHandlerListener.giveCompass(p);

            // Teleport to spawn
            p.teleport(spawnLocation);

            // Reset health, food, saturation and exhaustion
            p.setHealth(20);
            p.setFoodLevel(20);
            p.setSaturation(20);
            p.setExhaustion(0);

            // Reset XP
            p.setExp(0);
            p.setLevel(0);

            // Set gamemode to survival
            p.setGameMode(GameMode.SURVIVAL);
        }

        // Set time to 0 & enable daylight cycle
        overworld.setTime(0);
        overworld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);

        // Kill all dropped entities
        for (Entity en : overworld.getEntities()) {
            if (en instanceof Item) {
                en.remove();
            }
        }

        return true;
    }
}
