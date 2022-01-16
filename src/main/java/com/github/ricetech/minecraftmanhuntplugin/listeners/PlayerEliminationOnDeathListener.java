package com.github.ricetech.minecraftmanhuntplugin.listeners;

import com.github.ricetech.minecraftmanhuntplugin.MinecraftManhuntPlugin;
import com.github.ricetech.minecraftmanhuntplugin.commands.internal.SelfEliminateCommand;
import com.github.ricetech.minecraftmanhuntplugin.commands.internal.TeamTpCommand;
import com.github.ricetech.minecraftmanhuntplugin.data.ManhuntTeam;
import com.github.ricetech.minecraftmanhuntplugin.data.ScoreKeeper;
import com.github.ricetech.minecraftmanhuntplugin.data.TeamManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerEliminationOnDeathListener implements Listener {
    public void sendDeathCauseMsg(Player p) {
        p.sendMessage("What did you die to?");
        ComponentBuilder builderGuideMsg = new ComponentBuilder(MinecraftManhuntPlugin.GAME_MSG_COLOR + "Check the ");

        TextComponent linkComponent = new TextComponent(ChatColor.UNDERLINE + "" + MinecraftManhuntPlugin.GAME_MSG_COLOR + "Guide for Players");
        linkComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to open the Guide for Players")));
        linkComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/ricetech/MinecraftManhuntPlugin/wiki/Guide-for-Players"));

        TextComponent guideMsg2Component = new TextComponent(ChatColor.RESET + "" + MinecraftManhuntPlugin.GAME_MSG_COLOR + " if you aren't sure what to select.");

        builderGuideMsg.append(linkComponent).append(guideMsg2Component);
        p.spigot().sendMessage(builderGuideMsg.create());

        ComponentBuilder builderDeathConfirmMsg = new ComponentBuilder("Click one: ");

        TextComponent playerComponent = new TextComponent("[Player]");
        playerComponent.setColor(ChatColor.RED);
        playerComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + MinecraftManhuntPlugin.SELF_ELIMINATE_COMMAND_ALIAS));
        playerComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click if you died to a player")));

        TextComponent naturalCausesComponent = new TextComponent("[Natural Causes]");
        naturalCausesComponent.setColor(ChatColor.DARK_BLUE);
        naturalCausesComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + MinecraftManhuntPlugin.TP_OPTIONS_COMMAND_ALIAS));
        naturalCausesComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click if you died to natural causes")));

        builderDeathConfirmMsg
                .append(playerComponent)
                .append(" ")
                .append(naturalCausesComponent);

        TeamTpCommand.setEligibility(p.getName(), true);
        SelfEliminateCommand.setEligibility(p.getName(), true);

        p.spigot().sendMessage(builderDeathConfirmMsg.create());
    }

    @EventHandler
    public void eliminationMsgOnDeath(PlayerDeathEvent event) {
        if (MinecraftManhuntPlugin.isGameInProgress) {
            Player victim = event.getEntity();
            Player killer = victim.getKiller();
            ManhuntTeam victimTeam = TeamManager.getTeam(victim);
            ScoreKeeper.addDeath(victim);
            if (killer != null) {
                // Kill Spectators who decide to kill non-spectators
                if (TeamManager.getTeam(killer) == ManhuntTeam.SPECTATORS && victimTeam != ManhuntTeam.SPECTATORS) {
                    killer.setHealth(0);
                    ScoreKeeper.removeDeath(victim);
                    Bukkit.broadcastMessage(MinecraftManhuntPlugin.WARNING_MSG_COLOR + "Spectators are not allowed to kill other players!");
                } else {
                    // Increment killer's kills
                    ScoreKeeper.addKill(killer);
                    // Eliminate Runners
                    if (victimTeam == ManhuntTeam.RUNNERS) {
                        TeamManager.eliminatePlayer(victim);
                    }
                    // No other actions - teleports are not allowed after a death to another player
                }
            } else {
                // Ask if this death occurred because of a player or not
                sendDeathCauseMsg(victim);
            }
        }
    }
}
