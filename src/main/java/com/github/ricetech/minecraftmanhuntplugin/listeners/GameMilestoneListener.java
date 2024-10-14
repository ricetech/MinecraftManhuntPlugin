package com.github.ricetech.minecraftmanhuntplugin.listeners;

import com.github.ricetech.minecraftmanhuntplugin.MinecraftManhuntPlugin;
import com.github.ricetech.minecraftmanhuntplugin.commands.game.StopGameCommand;
import com.github.ricetech.minecraftmanhuntplugin.data.ManhuntMilestone;
import com.github.ricetech.minecraftmanhuntplugin.data.ManhuntTeam;
import com.github.ricetech.minecraftmanhuntplugin.data.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

import java.util.Collection;
import java.util.Map;

@SuppressWarnings("unused")
public class GameMilestoneListener implements Listener {
    public static final String PREFIX_I = "The Runners have progressed by ";
    public static final String PREFIX_W = "Runners win by ";
    public static final Map<ManhuntMilestone, String> ANNOUNCEMENTS = Map.ofEntries(
            Map.entry(ManhuntMilestone.ENTER_NETHER,  "making it to the Nether"),
            Map.entry(ManhuntMilestone.FORTRESS,  "finding a Fortress"),
            Map.entry(ManhuntMilestone.BLAZE_ROD,  "acquiring a Blaze Rod"),
            Map.entry(ManhuntMilestone.STRONGHOLD,  "finding a Stronghold"),
            Map.entry(ManhuntMilestone.THE_END,  "entering The End"),
            Map.entry(ManhuntMilestone.KILL_DRAGON,  "killing the Ender Dragon")
    );
    /**
     * Stops the game or announces intermediate milestones achieved by the Runners team
     */
    @EventHandler
    public void handleGameMilestone(PlayerAdvancementDoneEvent event) {
        if (MinecraftManhuntPlugin.isGameInProgress) {
            Player p = event.getPlayer();
            Advancement a = event.getAdvancement();
            Collection<String> criteria = a.getCriteria();
            ManhuntTeam team = TeamManager.getTeam(p);
            // Runners win even if Hunters kill the dragon
            if (MinecraftManhuntPlugin.currentMilestone == ManhuntMilestone.KILL_DRAGON && criteria.contains(MinecraftManhuntPlugin.milestoneAdvancements.get(ManhuntMilestone.KILL_DRAGON))) {
                StopGameCommand.stopGame(null, PREFIX_W + ANNOUNCEMENTS.get(MinecraftManhuntPlugin.currentMilestone));
            } else if (team == ManhuntTeam.RUNNERS) {  // Eliminated players cannot contribute towards milestones/objectives
                // Check Objectives
                if (criteria.contains(MinecraftManhuntPlugin.milestoneAdvancements.get(MinecraftManhuntPlugin.currentMilestone))) {
                    StopGameCommand.stopGame(null, PREFIX_W + ANNOUNCEMENTS.get(MinecraftManhuntPlugin.currentMilestone));
                }
                // Check Milestones
                for (ManhuntMilestone m : MinecraftManhuntPlugin.milestones.get(MinecraftManhuntPlugin.currentMilestone)) {
                    if (criteria.contains(MinecraftManhuntPlugin.milestoneAdvancements.get(m))) {
                        Bukkit.broadcastMessage(MinecraftManhuntPlugin.GAME_MSG_COLOR + "Manhunt: " + PREFIX_I + ANNOUNCEMENTS.get(m) + "!");
                    }
                }
            }
        }
    }
}
