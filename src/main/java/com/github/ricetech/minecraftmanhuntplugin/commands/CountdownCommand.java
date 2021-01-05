package com.github.ricetech.minecraftmanhuntplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

/**
 * Runs a countdown for the given number of seconds.
 * Reminders of remaining time are given at 30 second intervals, 45 seconds, 15 seconds and each of the last 10 seconds.
 */
public class CountdownCommand implements CommandExecutor {
    private static BukkitTask task;
    private final JavaPlugin plugin;

    public CountdownCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    private static void deleteTask() {
        task = null;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length != 1) {
            return false;
        }

        int seconds = Integer.parseInt(args[0]);

        // Cancel any existing timers
        if (task != null) {
            task.cancel();
            Bukkit.broadcastMessage("The previous timer was cancelled.");
        }

        task = new CountdownRunnable(seconds).runTaskTimer(plugin, 0, 20);
        Bukkit.broadcastMessage("Started a countdown for " + seconds + " seconds");

        return true;
    }

    private static class CountdownRunnable extends BukkitRunnable {
        int remainingTime;

        public CountdownRunnable(int counterTime) {
            this.remainingTime = counterTime;
        }

        @Override
        public void run() {
            if (remainingTime <= 0) {
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
