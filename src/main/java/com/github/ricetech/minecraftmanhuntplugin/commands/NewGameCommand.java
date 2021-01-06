package com.github.ricetech.minecraftmanhuntplugin.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
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

        ComponentBuilder builderSelectTeamMsg = new ComponentBuilder("Select a team:");

        TextComponent runnersComponent = new TextComponent("[RUNNERS]");
        runnersComponent.setColor(ChatColor.DARK_BLUE);
        runnersComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/changeteam runners"));

        TextComponent eliminatedComponent = new TextComponent("[ELIMINATED]");
        eliminatedComponent.setColor(ChatColor.DARK_AQUA);
        eliminatedComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/changeteam eliminated"));

        TextComponent huntersComponent = new TextComponent("[HUNTERS]");
        huntersComponent.setColor(ChatColor.RED);
        huntersComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/changeteam hunters"));

        TextComponent spectatorsComponent = new TextComponent("[SPECTATORS]");
        spectatorsComponent.setColor(ChatColor.GOLD);
        spectatorsComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/changeteam spectators"));

        builderSelectTeamMsg
                .append(" ")
                .append(runnersComponent)
                .append(" ")
                .append(eliminatedComponent)
                .append(" ")
                .append(huntersComponent)
                .append(" ")
                .append(spectatorsComponent);

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.spigot().sendMessage(builderSelectTeamMsg.create());
        }

        return true;
    }
}