package com.github.ricetech.minecraftmanhuntplugin.data;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Objects;

public class ScoreKeeper {
    private static final Scoreboard mainScoreboard = Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard();
    private static Objective kills;
    private static Objective deaths;

    public static void init() {
        // Create kills if it doesn't exist already
        kills = mainScoreboard.getObjective("kills");
        if (kills == null) {
            kills = mainScoreboard.registerNewObjective("kills", "dummy", "Kills");
        }
        kills.setDisplaySlot(DisplaySlot.PLAYER_LIST);

        // Create deaths if it doesn't exist already
        deaths = mainScoreboard.getObjective("deaths");
        if (deaths == null) {
            deaths = mainScoreboard.registerNewObjective("deaths", "dummy", "Deaths");
        }
        deaths.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public static Scoreboard getMainScoreboard() {
        return mainScoreboard;
    }

    /**
     * Sets the scores of all scoreboard entries to 0. Also remove offline players from the scoreboard.
     */
    public static void resetScores() {
        for (String entry : mainScoreboard.getEntries()) {
            if (Bukkit.getPlayerExact(entry) == null) {
                // Remove offline player
                mainScoreboard.resetScores(entry);
            } else {
                kills.getScore(entry).setScore(0);
                deaths.getScore(entry).setScore(0);
            }
        }
    }

    /**
     * Reset the scores of a particular player p.
     *
     * @param p - The player to reset the scores of.
     */
    public static void resetPlayer(Player p) {
        kills.getScore(p.getName()).setScore(0);
        deaths.getScore(p.getName()).setScore(0);
    }

    /**
     * Increments the Player p's kill counter by 1.
     *
     * @param p - The player to add a kill to.
     */
    public static void addKill(Player p) {
        Score pKills = kills.getScore(p.getName());
        pKills.setScore(pKills.getScore() + 1);
    }

    /**
     * Increments the Player p's death counter by 1.
     *
     * @param p - The player to add a kill to.
     */
    public static void addDeath(Player p) {
        Score pDeaths = deaths.getScore(p.getName());
        pDeaths.setScore(pDeaths.getScore() + 1);
    }

    /**
     * Decrements the Player p's death counter by 1.
     *
     * @param p - The player to add a kill to.
     */
    public static void removeDeath(Player p) {
        Score pDeaths = deaths.getScore(p.getName());
        pDeaths.setScore(pDeaths.getScore() < 1 ? 0 : pDeaths.getScore() - 1);
    }

    /**
     * Add player p if they aren't on the scoreboard already.
     *
     * @param p - The player to add.
     */
    public static void addNewPlayer(Player p) {
        Score pDeaths = deaths.getScore(p.getName());
        if (pDeaths.getScore() == 0) {
            pDeaths.setScore(0);
        }
    }
}
