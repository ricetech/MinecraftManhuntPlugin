package com.github.ricetech.minecraftmanhuntplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NewGameCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String labels, String[] args) {
        if (args.length != 0) {
            return false;
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            TeamSwitchCommand.sendTeamSelectMsg(p);
        }

        return true;
    }
}
