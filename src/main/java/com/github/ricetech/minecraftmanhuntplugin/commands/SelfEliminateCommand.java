package com.github.ricetech.minecraftmanhuntplugin.commands;

import com.github.ricetech.minecraftmanhuntplugin.MinecraftManhuntPlugin;
import com.github.ricetech.minecraftmanhuntplugin.data.ManhuntTeam;
import com.github.ricetech.minecraftmanhuntplugin.data.TeamManager;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class SelfEliminateCommand implements CommandExecutor {
    private final TeamManager teamManager;

    private final Map<String, Boolean> eligibility = new HashMap<>();

    public SelfEliminateCommand(MinecraftManhuntPlugin manhuntPlugin) {
        this.teamManager = manhuntPlugin.getTeamManager();
    }

    public boolean getEligibility(String entry) {
        return this.eligibility.getOrDefault(entry, false);
    }

    public void setEligibility(String entry, boolean eligibility) {
        this.eligibility.put(entry, eligibility);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p;

        if (!(sender instanceof Player)) {
            MinecraftManhuntPlugin.sendOnlyPlayersErrorMsg(sender);
            return true;
        } else {
            p = ((Player) sender);
        }

        ManhuntTeam team = teamManager.getTeam(p);

        if (team == null) {
            TextComponent errorMsg = new TextComponent("Error: You aren't on a team. Use /changeteam to join a team.");
            errorMsg.setColor(net.md_5.bungee.api.ChatColor.RED);
            p.spigot().sendMessage(errorMsg);
            return true;
        }

        if (!eligibility.getOrDefault(p.getName(), false)) {
            sender.sendMessage(ChatColor.RED + "Error: You are not eligible for elimination.");
            return true;
        }

        if (team == ManhuntTeam.RUNNERS) {
            eligibility.put(p.getName(), false);
            teamManager.eliminatePlayer(p);
        } else {
            TextComponent errorMsg = new TextComponent("Error: You cannot be eliminated unless you are a Runner.");
            errorMsg.setColor(net.md_5.bungee.api.ChatColor.RED);
            p.spigot().sendMessage(errorMsg);
            return true;
        }

        return true;
    }
}
