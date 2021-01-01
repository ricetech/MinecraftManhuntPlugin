package com.github.ricetech.minecraftmanhuntplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class CountdownCommand implements CommandExecutor {
    private final JavaPlugin plugin;

    public CountdownCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length != 1) {
            return false;
        }

        int seconds = Integer.parseInt(args[0]);
        Bukkit.broadcastMessage("Started countdown for " + seconds + " seconds");
        new CountdownRunnable(seconds).runTaskTimer(plugin, 0, 20);

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
            } else if (remainingTime == 45 || remainingTime % 30 == 0 || remainingTime == 15 || remainingTime < 11) {
                Bukkit.broadcastMessage(remainingTime + " seconds remain");
            }
            remainingTime--;
        }
    }
}
