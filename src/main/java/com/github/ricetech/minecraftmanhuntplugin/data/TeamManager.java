package com.github.ricetech.minecraftmanhuntplugin.data;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class TeamManager {
    private Team runners;
    private Team eliminated;
    private Team hunters;
    private Team spectators;

    public TeamManager() {
        //noinspection ConstantConditions
        Scoreboard mainScoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        // Get all teams from scoreboard to see if they exist or not
        runners = mainScoreboard.getTeam("runners");
        eliminated = mainScoreboard.getTeam("eliminated");
        hunters = mainScoreboard.getTeam("hunters");
        spectators = mainScoreboard.getTeam("spectators");

        // Create teams if they do not exist
        if (runners == null) {
            runners = mainScoreboard.registerNewTeam("runners");
        }

        if (eliminated == null) {
            eliminated = mainScoreboard.registerNewTeam("eliminated");
        }

        if (hunters == null) {
            hunters = mainScoreboard.registerNewTeam("hunters");
        }

        if (spectators == null) {
            spectators = mainScoreboard.registerNewTeam("spectators");
        }

        // Init team settings
        runners.setDisplayName("Runners");
        eliminated.setDisplayName("Eliminated");
        hunters.setDisplayName("Hunters");
        spectators.setDisplayName("Spectators");

        runners.setColor(ChatColor.DARK_BLUE);
        eliminated.setColor(ChatColor.DARK_AQUA);
        hunters.setColor(ChatColor.RED);
        spectators.setColor(ChatColor.GOLD);

        runners.setAllowFriendlyFire(true);
        eliminated.setAllowFriendlyFire(true);
        hunters.setAllowFriendlyFire(true);
        spectators.setAllowFriendlyFire(true);

        runners.setCanSeeFriendlyInvisibles(true);
        eliminated.setCanSeeFriendlyInvisibles(true);
        hunters.setCanSeeFriendlyInvisibles(true);
        spectators.setCanSeeFriendlyInvisibles(true);

        runners.setOption(Team.Option.DEATH_MESSAGE_VISIBILITY, Team.OptionStatus.ALWAYS);
        eliminated.setOption(Team.Option.DEATH_MESSAGE_VISIBILITY, Team.OptionStatus.ALWAYS);
        hunters.setOption(Team.Option.DEATH_MESSAGE_VISIBILITY, Team.OptionStatus.ALWAYS);
        spectators.setOption(Team.Option.DEATH_MESSAGE_VISIBILITY, Team.OptionStatus.FOR_OWN_TEAM);

        runners.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
        eliminated.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
        hunters.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
        spectators.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);

        runners.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.ALWAYS);
        eliminated.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.ALWAYS);
        hunters.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.ALWAYS);
        spectators.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.FOR_OWN_TEAM);

    }
}
