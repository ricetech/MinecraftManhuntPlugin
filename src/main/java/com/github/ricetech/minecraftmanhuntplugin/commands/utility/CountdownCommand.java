package com.github.ricetech.minecraftmanhuntplugin.commands.utility;

import com.github.ricetech.minecraftmanhuntplugin.MinecraftManhuntPlugin;
import com.github.ricetech.minecraftmanhuntplugin.data.ManhuntTeam;
import com.github.ricetech.minecraftmanhuntplugin.data.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Runs a countdown for the given number of seconds.
 * Reminders of remaining time are given at 30 second intervals, 45 seconds, 15 seconds and each of the last 10 seconds.
 */
public class CountdownCommand implements CommandExecutor {
    public final long COUNTDOWN_START_DELAY_SECONDS = 5L;

    private static BukkitTask task;
    private final JavaPlugin plugin;

    public CountdownCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    private static void deleteTask() {
        task = null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        int seconds;
        boolean freezeHunters = false;

        if (args.length < 1 || args.length > 2) {
            return false;
        }

        try {
            seconds = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            MinecraftManhuntPlugin.sendErrorMsg(sender, args[0] + " is not a number");
            return false;
        }

        if (args.length > 1) {
            if (!args[1].toLowerCase().matches("^true$|^false$")) {
                MinecraftManhuntPlugin.sendErrorMsg(sender, "Argument for 'freeze/reset Hunters' must be 'true' or 'false'");
                return false;
            } else {
                freezeHunters = Boolean.parseBoolean(args[1]);
            }
        }

        // Cancel any existing timers
        if (task != null) {
            task.cancel();
            Bukkit.broadcastMessage("The previous timer was cancelled.");
        }

        @SuppressWarnings("ConstantConditions") String secondsWordDelay = COUNTDOWN_START_DELAY_SECONDS == 1 ? " second" : " seconds";
        String secondsWordDuration = seconds == 1 ? " second" : " seconds";

        task = new CountdownRunnable(seconds, freezeHunters).runTaskTimer(plugin, COUNTDOWN_START_DELAY_SECONDS * 20L, 20L);
        Bukkit.broadcastMessage("A countdown for " + seconds + secondsWordDuration + " will start in " +
                COUNTDOWN_START_DELAY_SECONDS + secondsWordDelay + ".");

        return true;
    }

    private static class CountdownRunnable extends BukkitRunnable {
        private final boolean restrictHunters;

        private final int initTime;
        private int remainingTime;
        private final String secondsWord;

        public CountdownRunnable(int counterTime, boolean restrictHunters) {
            this.restrictHunters = restrictHunters;
            this.remainingTime = counterTime;
            this.initTime = counterTime;
            this.secondsWord = counterTime == 1 ? " second." : " seconds.";
        }

        private void restrictHunters() {
            List<Player> hunters = TeamManager.listTeamPlayers(ManhuntTeam.HUNTERS);
            for (Player hunter : hunters) {
                hunter.setGameMode(GameMode.SPECTATOR);
                hunter.setFlySpeed(0f);
                hunter.teleport(Bukkit.getWorlds().getFirst().getSpawnLocation());
            }
        }

        private void unRestrictHunters() {
            List<Player> hunters = TeamManager.listTeamPlayers(ManhuntTeam.HUNTERS);
            for (Player hunter : hunters) {
                hunter.setFlySpeed(0.1f);
                ResetCommand.resetPlayer(hunter);
            }
        }

        @Override
        public void run() {
            if (this.restrictHunters) {
                restrictHunters();
            }
            if (remainingTime == initTime) {
                Bukkit.broadcastMessage("Countdown started for " + initTime + secondsWord);
            }
            if (remainingTime <= 0) {
                if (this.restrictHunters) {
                    unRestrictHunters();
                }
                Bukkit.broadcastMessage("Countdown finished. GO!");
                this.cancel();
                deleteTask();
            } else if (remainingTime == 1) {
                Bukkit.broadcastMessage("1 second left");
            } else if (remainingTime == 45 || remainingTime % 30 == 0 || remainingTime == 15 || remainingTime < 11) {
                Bukkit.broadcastMessage(remainingTime + " seconds left");
            }
            remainingTime--;
        }
    }
}
