package com.github.ricetech.minecraftmanhuntplugin.commands.utility;

import com.github.ricetech.minecraftmanhuntplugin.MinecraftManhuntPlugin;
import com.github.ricetech.minecraftmanhuntplugin.commands.internal.SelfEliminateCommand;
import com.github.ricetech.minecraftmanhuntplugin.commands.internal.TeamTpCommand;
import com.github.ricetech.minecraftmanhuntplugin.commands.player.TeamSwitchCommand;
import com.github.ricetech.minecraftmanhuntplugin.commands.player.TrackCommand;
import com.github.ricetech.minecraftmanhuntplugin.data.ManhuntTeam;
import com.github.ricetech.minecraftmanhuntplugin.data.ScoreKeeper;
import com.github.ricetech.minecraftmanhuntplugin.data.TeamManager;
import com.github.ricetech.minecraftmanhuntplugin.listeners.CompassInventoryHandlerListener;
import com.github.ricetech.minecraftmanhuntplugin.listeners.GameMilestoneListener;
import com.github.ricetech.minecraftmanhuntplugin.listeners.PlayerDeathLocationStorageListener;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

/**
 * Reset the world and all players in preparation for starting a new game of Manhunt.
 */
@SuppressWarnings("unused")
public class ResetCommand implements CommandExecutor {
    private final JavaPlugin plugin;

    public ResetCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public static void resetPlayer(Player p) {
        Location worldSpawn = Bukkit.getWorlds().getFirst().getSpawnLocation();
        // Reset respawn points (i.e. reset bed spawns)
        p.setRespawnLocation(worldSpawn);

        // Remove effects
        for (PotionEffect effect : p.getActivePotionEffects()) {
            if (effect.getType() != PotionEffectType.NIGHT_VISION) {
                p.removePotionEffect(effect.getType());
            }
        }

        // Clear inventory
        p.getInventory().clear();

        // Give compass
        CompassInventoryHandlerListener.giveCompass(p);

        // Set gamemode if necessary
        ManhuntTeam team = TeamManager.getTeam(p);
        if (team == null) {
            // Add players not on any team to Spectators
            // Also allow them to still select a team

            TeamSwitchCommand.sendTeamSelectMsg(p);

            p.sendMessage(MinecraftManhuntPlugin.WARNING_MSG_COLOR + "Alert: You did not select a team and have therefore " +
                    "been added to the Spectators team automatically. You can use the message above to join " +
                    "a different team.");

            TeamManager.editTeam(p, ManhuntTeam.SPECTATORS);
        } else if (team == ManhuntTeam.SPECTATORS) {
            p.setGameMode(GameMode.SPECTATOR);
        } else {
            // Set gamemode to survival
            p.setGameMode(GameMode.SURVIVAL);
        }

        // Un-eliminate all eliminated players
        if (team == ManhuntTeam.ELIMINATED) {
            TeamManager.editTeam(p, ManhuntTeam.RUNNERS);
        }

        // Teleport to spawn
        p.teleport(Bukkit.getServer().getWorlds().getFirst().getSpawnLocation());

        // Reset health, food, saturation and exhaustion
        p.setHealth(20);
        p.setFoodLevel(20);
        p.setSaturation(20);
        p.setExhaustion(0);

        // Reset XP
        p.setExp(0);
        p.setLevel(0);
    }

    public static void runReset(JavaPlugin plugin) {
        World overworld = Bukkit.getServer().getWorlds().getFirst();

        for (Player p : Bukkit.getOnlinePlayers()) {
            resetPlayer(p);
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

        overworld.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);

        // Revoke all advancements unless they're a spectator (in which case there's no point)
        Bukkit.getScheduler().runTaskLater(plugin, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement revoke @a[team=!" + TeamManager.getSpectators().getName() + "] everything"), 10L);

        // Grant all advancements to spectators
        Bukkit.getScheduler().runTaskLater(plugin, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant @a[team=" + TeamManager.getSpectators().getName() + "] everything"), 20L);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (TeamManager.getTeam(p) == ManhuntTeam.SPECTATORS) {
                    p.sendMessage(MinecraftManhuntPlugin.WARNING_MSG_COLOR + "You have been granted all advancements to " +
                            "avoid spoiling game progress to other players. Unfortunately, there is no way to do that " +
                            "without blasting you with sound. We apologize, and you can turn up your volume in a minute " +
                            "or two once the popups stop appearing.");
                }
            }
        }, 20L);

        Bukkit.getScheduler().runTaskLater(plugin, () -> overworld.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, true), 30L);

        // Reset plugin state
        PlayerDeathLocationStorageListener.reset();
        TrackCommand.reset();
        SelfEliminateCommand.reset();
        TeamSwitchCommand.reset();
        TeamTpCommand.reset();
        ScoreKeeper.resetScores();
        GameMilestoneListener.reset();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length > 0) {
            return false;
        }

        if (MinecraftManhuntPlugin.isGameInProgress) {
            MinecraftManhuntPlugin.sendErrorMsg(sender, "Cannot reset while game is in progress.");
            return true;
        }

        runReset(this.plugin);

        return true;
    }
}
