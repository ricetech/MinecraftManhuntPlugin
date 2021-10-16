package com.github.ricetech.minecraftmanhuntplugin.commands.internal;

import com.github.ricetech.minecraftmanhuntplugin.MinecraftManhuntPlugin;
import com.github.ricetech.minecraftmanhuntplugin.commands.internal.TeamTpCommand;
import com.github.ricetech.minecraftmanhuntplugin.data.TeamManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TpOptionsCommand implements CommandExecutor {

    public TpOptionsCommand() {
    }

    public static void sendTpOptions(Player p) {
        // Get list of players to tp to
        List<Player> playerOptions = TeamManager.listTeamPlayers(TeamManager.getTeam(p), p);

        if (playerOptions.size() == 0) {
            // Teleport to last death location
            p.sendMessage(MinecraftManhuntPlugin.WARNING_MSG_COLOR + "Warning: There are no available teammates to teleport to.");
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
            ComponentBuilder selectPlayerMsg = new ComponentBuilder("Click on a teammate to teleport to:");
            net.md_5.bungee.api.ChatColor teamColor = MinecraftManhuntPlugin.getBungeeCordTeamColor(TeamManager.getTeam(p));

            for (Player playerOption : playerOptions) {
                TextComponent playerComponent = new TextComponent("[" + playerOption.getName() + "]");
                playerComponent.setColor(teamColor);
                playerComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" +
                        MinecraftManhuntPlugin.TEAM_TP_COMMAND_ALIAS + " " + playerOption.getName()));

                // Append to message builder
                selectPlayerMsg.append(" ");
                selectPlayerMsg.append(playerComponent);
            }
            p.spigot().sendMessage(selectPlayerMsg.create());
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length != 0) {
            return false;
        }

        // Ensure sender is a player, then cast sender to Player
        if (!(sender instanceof Player p)) {
            MinecraftManhuntPlugin.sendOnlyPlayersErrorMsg(sender);
            return true;
        }

        // Eligibility
        if (!TeamTpCommand.getEligibility(p.getName())) {
            MinecraftManhuntPlugin.sendErrorMsg(sender, "You are not eligible to teleport.");
            return true;
        }

        Bukkit.broadcastMessage(MinecraftManhuntPlugin.getBukkitTeamColor(TeamManager.getTeam(p)) + p.getName() +
                MinecraftManhuntPlugin.GAME_MSG_COLOR + "'s death was confirmed to have been caused naturally. " +
                "They will be able to teleport to their teammate shortly.");
        sendTpOptions(p);

        return true;
    }
}
