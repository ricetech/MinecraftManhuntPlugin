package com.github.ricetech.minecraftmanhuntplugin.commands;

import com.github.ricetech.minecraftmanhuntplugin.MinecraftManhuntPlugin;
import com.github.ricetech.minecraftmanhuntplugin.data.TeamManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

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

        // Get list of players to tp to
        List<Player> playerOptions = teamManager.listTeamPlayers(teamManager.getTeam(p), p);

        // Build message
        ComponentBuilder selectPlayerMsg = new ComponentBuilder("Select a teammate to teleport to:");
        net.md_5.bungee.api.ChatColor teamColor = MinecraftManhuntPlugin.getBungeeCordTeamColor(teamManager.getTeam(p));

        for (Player playerOption : playerOptions) {
            TextComponent playerComponent = new TextComponent(playerOption.getName());
            playerComponent.setColor(teamColor);
            playerComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" +
                    MinecraftManhuntPlugin.TEAM_TP_COMMAND_ALIAS + " " + playerOption.getName()));

            // Append to message builder
            selectPlayerMsg.append(" ");
            selectPlayerMsg.append(playerComponent);
        }
        p.spigot().sendMessage(selectPlayerMsg.create());
        return true;
    }
}
