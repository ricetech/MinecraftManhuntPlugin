package com.github.ricetech.minecraftmanhuntplugin.commands;

import com.github.ricetech.minecraftmanhuntplugin.MinecraftManhuntPlugin;
import com.github.ricetech.minecraftmanhuntplugin.data.TeamManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpOptionsCommand implements CommandExecutor {
    private final TeamManager teamManager;
    private final MinecraftManhuntPlugin manhuntPlugin;

    public TpOptionsCommand(MinecraftManhuntPlugin manhuntPlugin) {
        this.manhuntPlugin = manhuntPlugin;
        this.teamManager = this.manhuntPlugin.getTeamManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player p;

        if (args.length != 0) {
            return false;
        }

        // Ensure sender is a player, then cast sender to Player
        if (!(sender instanceof Player)) {
            MinecraftManhuntPlugin.sendOnlyPlayersErrorMsg(sender);
            return true;
        } else {
            p = ((Player) sender);
        }

        // Eligibility
        if (!TeamTpCommand.getEligibility(p.getName())) {
            sender.sendMessage(ChatColor.RED + "Error: You are not eligible to teleport.");
            return true;
        }

        return true;
    }
}
