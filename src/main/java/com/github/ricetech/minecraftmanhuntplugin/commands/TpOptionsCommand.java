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

        if (playerOptions.size() == 0) {
            // Teleport to last death location
            p.sendMessage(ChatColor.GOLD + "Warning: There are no available teammates to teleport to.");
            p.sendMessage("You may instead teleport to where you died. You will be given 25 seconds of invincibility " +
                    "to protect you against any mobs located where you died.");
            p.sendMessage(ChatColor.RED + "Make sure you did not die in an unsafe location before teleporting.");

            ComponentBuilder teleportMsg = new ComponentBuilder("Would you like to teleport to your death location? ");

            TextComponent yes = new TextComponent("[Yes]");
            yes.setColor(net.md_5.bungee.api.ChatColor.GREEN);
            yes.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + MinecraftManhuntPlugin.TEAM_TP_COMMAND_ALIAS
                    + " " + TeamTpCommand.SELF_TP_ARG));
            TextComponent no = new TextComponent("[No]");
            no.setColor(net.md_5.bungee.api.ChatColor.RED);
            no.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + MinecraftManhuntPlugin.RESET_ELIGIBILITY_COMMAND_ALIAS));

            teleportMsg.append(" ").append(yes).append(" ").append(no);

            p.spigot().sendMessage(teleportMsg.create());
        } else {
            // Send message with list of tp options
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
        }

        return true;
    }
}
