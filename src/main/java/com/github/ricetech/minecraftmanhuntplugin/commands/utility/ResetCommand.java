package com.github.ricetech.minecraftmanhuntplugin.commands.utility;

import com.github.ricetech.minecraftmanhuntplugin.commands.internal.SelfEliminateCommand;
import com.github.ricetech.minecraftmanhuntplugin.commands.player.TeamSwitchCommand;
import com.github.ricetech.minecraftmanhuntplugin.commands.internal.TeamTpCommand;
import com.github.ricetech.minecraftmanhuntplugin.commands.player.TrackCommand;
import com.github.ricetech.minecraftmanhuntplugin.data.ManhuntTeam;
import com.github.ricetech.minecraftmanhuntplugin.data.ScoreKeeper;
import com.github.ricetech.minecraftmanhuntplugin.data.TeamManager;
import com.github.ricetech.minecraftmanhuntplugin.listeners.CompassInventoryHandlerListener;
import com.github.ricetech.minecraftmanhuntplugin.listeners.PlayerDeathLocationStorageListener;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

/**
 * Reset the world and all players in preparation for starting a new game of Manhunt.
 */
@SuppressWarnings("unused")
public class ResetCommand implements CommandExecutor {

    public ResetCommand() { }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length > 0) {
            return false;
        }

        World overworld = Bukkit.getServer().getWorlds().get(0);

        Location spawnLocation = overworld.getSpawnLocation();

        Team team;

        for (Player p : Bukkit.getOnlinePlayers()) {
            // Remove effects
            for (PotionEffect effect : p.getActivePotionEffects()) {
                p.removePotionEffect(effect.getType());
            }

            // Clear inventory
            p.getInventory().clear();

            // Give compass
            CompassInventoryHandlerListener.giveCompass(p);

            // Set gamemode if necessary
            team = ScoreKeeper.getMainScoreboard().getEntryTeam(p.getName());
            if (team == null) {
                // Add players not on any team to Spectators
                // Also allow them to still select a team

                TeamSwitchCommand.sendTeamSelectMsg(p);

                p.sendMessage(ChatColor.GOLD + "Alert: You did not select a team and have therefore " +
                                "been added to the Spectators team automatically. You can use the message above to join " +
                        "a different team.");

                TeamManager.editTeam(p, ManhuntTeam.SPECTATORS);
            } else if (team == TeamManager.getSpectators()) {
                p.setGameMode(GameMode.SPECTATOR);
            } else {
                // Set gamemode to survival
                p.setGameMode(GameMode.SURVIVAL);
            }

            // Un-eliminate all eliminated players
            if (team == TeamManager.getEliminated()) {
                TeamManager.editTeam(p, ManhuntTeam.RUNNERS);
            }

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
        }

        // Set time to 0 & enable daylight cycle
        overworld.setTime(0);
        overworld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);

        // Set weather
        overworld.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        overworld.setStorm(false);

        // Kill all dropped entities
        for (Entity en : overworld.getEntities()) {
            if (en instanceof Item) {
                en.remove();
            }
        }

        // Revoke all advancements
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement revoke @a everything");

        // Reset plugin state
        PlayerDeathLocationStorageListener.reset();
        TrackCommand.reset();
        SelfEliminateCommand.reset();
        TeamSwitchCommand.reset();
        TeamTpCommand.reset();
        ScoreKeeper.resetScores();

        return true;
    }
}
