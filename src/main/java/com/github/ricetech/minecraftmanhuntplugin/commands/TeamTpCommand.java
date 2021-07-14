package com.github.ricetech.minecraftmanhuntplugin.commands;

import com.github.ricetech.minecraftmanhuntplugin.MinecraftManhuntPlugin;
import com.github.ricetech.minecraftmanhuntplugin.data.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class TeamTpCommand implements CommandExecutor {
    @SuppressWarnings("FieldCanBeLocal")
    private static final long SAFETY_DELAY_SECONDS = 5L;

    private static final Map<String, Boolean> eligibility = new HashMap<>();

    private final TeamManager teamManager;
    private final MinecraftManhuntPlugin manhuntPlugin;

    public TeamTpCommand(MinecraftManhuntPlugin manhuntPlugin) {
        this.manhuntPlugin = manhuntPlugin;
        this.teamManager = this.manhuntPlugin.getTeamManager();
    }

    public static boolean getEligibility(String entry) {
        return eligibility.getOrDefault(entry, false);
    }

    public static void setEligibility(String entry, boolean isEligible) {
        eligibility.put(entry, isEligible);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p;

        if (args.length != 1) {
            return false;
        }

        // Ensure sender is a player, then cast sender to Player
        if (!(sender instanceof Player)) {
            MinecraftManhuntPlugin.sendOnlyPlayersErrorMsg(sender);
            return true;
        } else {
            p = ((Player) sender);
        }

        // Get Player object of target player, check if online
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Error: Target player does not exist or is offline. Try clicking on another player.");
            Bukkit.dispatchCommand(p, MinecraftManhuntPlugin.TP_OPTIONS_COMMAND_ALIAS);
            return true;
        }

        if (teamManager.getTeam(p) != teamManager.getTeam(target)) {
            sender.sendMessage(ChatColor.RED + "Error: Target player is not on your team. Try clicking on another player.");
            Bukkit.dispatchCommand(p, MinecraftManhuntPlugin.TP_OPTIONS_COMMAND_ALIAS);
            return true;
        }

        if (!eligibility.getOrDefault(p.getName(), false)) {
            sender.sendMessage(ChatColor.RED + "Error: You are not eligible to teleport.");
            return true;
        }

        eligibility.put(p.getName(), false);
        // Disable eligibility for /selfelim
        SelfEliminateCommand.setEligibility(p.getName(), false);

        new TeamTpRunnable(p, target).runTaskLater(this.manhuntPlugin, SAFETY_DELAY_SECONDS * 20L);

        // Alert players
        String secondsWord = SAFETY_DELAY_SECONDS == 1 ? " second" : " seconds";

        p.sendMessage(ChatColor.YELLOW + "Alert: You will be teleported to " + target.getName() + " in " +
                SAFETY_DELAY_SECONDS + secondsWord + ".");
        target.sendMessage(ChatColor.YELLOW + "Alert: " + p.getName() + " will be teleported to you in " +
                SAFETY_DELAY_SECONDS + secondsWord + ". Make sure you are in a safe location!");

        return true;
    }

    private static class TeamTpRunnable extends BukkitRunnable {
        private final Player p;
        private final Player target;

        public TeamTpRunnable(Player p, Player target) {
            this.p = p;
            this.target = target;
        }

        @Override
        public void run() {
            p.teleport(target);
        }
    }
}
