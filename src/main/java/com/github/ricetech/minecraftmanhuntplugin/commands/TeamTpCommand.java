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

public class TeamTpCommand implements CommandExecutor {
    @SuppressWarnings("FieldCanBeLocal")
    private final long SAFETY_DELAY_SECONDS = 5;

    private final TeamManager teamManager;
    private final MinecraftManhuntPlugin manhuntPlugin;

    public TeamTpCommand(MinecraftManhuntPlugin manhuntPlugin) {
        this.manhuntPlugin = manhuntPlugin;
        this.teamManager = this.manhuntPlugin.getTeamManager();
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
            sender.sendMessage(ChatColor.RED + "Error: Target player does not exist or is offline");
            return true;
        }

        if (teamManager.getTeam(p) != teamManager.getTeam(target)) {
            sender.sendMessage(ChatColor.RED + "Error: Target player is not on your team");
            return true;
        }

        new TeamTpRunnable(p, target).runTaskLater(this.manhuntPlugin, SAFETY_DELAY_SECONDS * 20);

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
