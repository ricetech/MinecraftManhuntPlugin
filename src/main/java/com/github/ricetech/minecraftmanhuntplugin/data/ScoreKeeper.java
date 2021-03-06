package com.github.ricetech.minecraftmanhuntplugin.data;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreKeeper {
    private final Scoreboard mainScoreboard;
    private Objective kills;
    private Objective deaths;

    public ScoreKeeper() {
        //noinspection ConstantConditions
        this.mainScoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        // Create kills if it doesn't exist already
        this.kills = mainScoreboard.getObjective("kills");
        if (this.kills == null) {
            this.kills = mainScoreboard.registerNewObjective("kills", "dummy", "Kills");
        }
        kills.setDisplaySlot(DisplaySlot.PLAYER_LIST);

        // Create deaths if it doesn't exist already
        this.deaths = mainScoreboard.getObjective("deaths");
        if (this.deaths == null) {
            this.deaths = mainScoreboard.registerNewObjective("deaths", "deathCount", "Deaths");
        }
        deaths.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public Scoreboard getMainScoreboard() {
        return mainScoreboard;
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

    /**
     * Increments the Player p's kill counter by 1.
     *
     * @param p - The player to add a kill to.
     */
    public void addKill(Player p) {
        Score pKills = kills.getScore(p.getName());
        pKills.setScore(pKills.getScore() + 1);
    }
}
