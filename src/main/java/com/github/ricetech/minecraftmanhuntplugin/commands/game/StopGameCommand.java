package com.github.ricetech.minecraftmanhuntplugin.commands.game;

import com.github.ricetech.minecraftmanhuntplugin.MinecraftManhuntPlugin;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StopGameCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length != 0) {
            return false;
        }

        if (!MinecraftManhuntPlugin.isGameInProgress) {
            MinecraftManhuntPlugin.sendErrorMsg(sender, "There is no game to stop.");
            return true;
        }

        MinecraftManhuntPlugin.isGameInProgress = false;
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendTitle(MinecraftManhuntPlugin.GAME_MSG_COLOR + "Game Over!", "The game was stopped manually.",
                    MinecraftManhuntPlugin.TITLE_FADE_IN, MinecraftManhuntPlugin.TITLE_STAY, MinecraftManhuntPlugin.TITLE_FADE_OUT);
        }
        Bukkit.broadcastMessage(MinecraftManhuntPlugin.GAME_MSG_COLOR + "Manhunt: Game Over! The game was stopped manually.");

        // Stop time
        World overworld = Bukkit.getServer().getWorlds().getFirst();
        overworld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        overworld.setTime(6000L);
        return true;
    }
}
