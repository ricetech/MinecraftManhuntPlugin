package com.github.ricetech.minecraftmanhuntplugin.data;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreKeeper {
    private Scoreboard mainScoreboard;
    private Objective kills;
    private Objective deaths;

    public ScoreKeeper() {
        //noinspection ConstantConditions
        this.mainScoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        // Create kills if it doesn't exist already
        this.kills = mainScoreboard.getObjective("kills");
        if (this.kills == null) {
            this.kills = mainScoreboard.registerNewObjective("kills", "playerKillCount", "Kills");
        }
        kills.setDisplaySlot(DisplaySlot.PLAYER_LIST);

        // Create deaths if it doesn't exist already
        this.deaths = mainScoreboard.getObjective("deaths");
        if (this.deaths == null) {
            this.deaths = mainScoreboard.registerNewObjective("deaths", "deathCount", "Deaths");
        }
        deaths.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    /**
     * Sets the scores of all currently online players to 0.
     */
    public void resetScores() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            kills.getScore(p.getName()).setScore(0);
            deaths.getScore(p.getName()).setScore(0);
        }
    }

    /**
     * Reset the scores of a particular player p.
     *
     * @param p - The player to reset the scores of.
     */
    public void resetPlayer(Player p) {
        kills.getScore(p.getName()).setScore(0);
        deaths.getScore(p.getName()).setScore(0);
    }
}
