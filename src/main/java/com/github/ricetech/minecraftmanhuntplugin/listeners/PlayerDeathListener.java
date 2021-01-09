package com.github.ricetech.minecraftmanhuntplugin.listeners;

import com.github.ricetech.minecraftmanhuntplugin.MinecraftManhuntPlugin;
import com.github.ricetech.minecraftmanhuntplugin.data.ScoreKeeper;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {
    private final MinecraftManhuntPlugin manhuntPlugin;
    private final ScoreKeeper scoreKeeper;

    public PlayerDeathListener(MinecraftManhuntPlugin manhuntPlugin) {
        this.manhuntPlugin = manhuntPlugin;
        this.scoreKeeper = manhuntPlugin.getScoreKeeper();
    }

    public static void sendDeathConfirmMsg(Player p) {
        p.sendMessage("Did you die to natural causes or to a player?");
        p.sendMessage("Remember, you must select [Player] even if the cause of death was indirect.");
        ComponentBuilder builderDeathConfirmMsg = new ComponentBuilder("Pick one: ");

        TextComponent naturalCausesComponent = new TextComponent("[Natural Causes]");
        naturalCausesComponent.setColor(ChatColor.DARK_BLUE);
        naturalCausesComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/"));

        TextComponent playerComponent = new TextComponent("[Player]");
        playerComponent.setColor(ChatColor.RED);
        playerComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/"));

        builderDeathConfirmMsg
                .append(" ")
                .append(naturalCausesComponent)
                .append(" ")
                .append(playerComponent);

        p.spigot().sendMessage(builderDeathConfirmMsg.create());
    }

    @EventHandler
    public void eliminationMsgOnDeath(PlayerDeathEvent event) {
        if (this.manhuntPlugin.isGameInProgress()) {
            Player victim = event.getEntity();
            Player killer = victim.getKiller();
            if (killer != null) {
                // Increment killer's kills
                this.scoreKeeper.addKill(killer);
                // No other actions - teleports are not allowed after a death to another player
            } else {
                // Ask if this death occurred because of a player or not
                sendDeathConfirmMsg(event.getEntity());
            }
        }
    }
}
