package com.github.ricetech.minecraftmanhuntplugin.data;

import com.github.ricetech.minecraftmanhuntplugin.MinecraftManhuntPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class TeamManager {
    private static final Scoreboard mainScoreboard = Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard();
    private static final Set<String> validTeams = new HashSet<>();
    private static Team runners;
    private static Team eliminated;
    private static Team hunters;
    private static Team spectators;

    public static void init() {
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

        runners.setColor(MinecraftManhuntPlugin.RUNNERS_COLOR);
        eliminated.setColor(MinecraftManhuntPlugin.ELIMINATED_COLOR);
        hunters.setColor(MinecraftManhuntPlugin.HUNTERS_COLOR);
        spectators.setColor(MinecraftManhuntPlugin.SPECTATORS_COLOR);

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

        for (ManhuntTeam team : ManhuntTeam.values()) {
            validTeams.add(team.name());
        }
    }

    public static Set<String> getValidTeams() {
        return validTeams;
    }

    public static Team getRunners() {
        return runners;
    }

    public static Team getEliminated() {
        return eliminated;
    }

    public static Team getHunters() {
        return hunters;
    }

    public static Team getSpectators() {
        return spectators;
    }

    public static ManhuntTeam getTeam(Player p) {
        return getTeam(p.getName());
    }

    public static ManhuntTeam getTeam(String playerName) {
        Team team = mainScoreboard.getEntryTeam(playerName);
        if (team == null) {
            return null;
        } else if (team.equals(runners)) {
            return ManhuntTeam.RUNNERS;
        } else if (team.equals(hunters)) {
            return ManhuntTeam.HUNTERS;
        } else if (team.equals(eliminated)) {
            return ManhuntTeam.ELIMINATED;
        } else if (team.equals(spectators)) {
            return ManhuntTeam.SPECTATORS;
        } else {
            return null;
        }
    }

    public static List<Player> listTeamPlayers(ManhuntTeam team, Player targetPlayer) {
        Team targetTeam = switch (team) {
            case RUNNERS -> runners;
            case HUNTERS -> hunters;
            case ELIMINATED -> eliminated;
            case SPECTATORS -> spectators;
        };

        ArrayList<Player> players = new ArrayList<>();

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (mainScoreboard.getEntryTeam(p.getName()) == targetTeam && p != targetPlayer) {
                players.add(p);
            }
        }

        return players;
    }

    public static List<Player> listTeamPlayers(ManhuntTeam team) {
        return listTeamPlayers(team, null);
    }

    public static Set<String> getTeamEntries(ManhuntTeam team) {
        Team targetTeam = switch (team) {
            case RUNNERS -> runners;
            case HUNTERS -> hunters;
            case ELIMINATED -> eliminated;
            case SPECTATORS -> spectators;
        };

        return targetTeam.getEntries();
    }

    public static void editTeam(Player p, ManhuntTeam team) {
        switch (team) {
            case RUNNERS -> {
                runners.addEntry(p.getName());
                p.setGameMode(GameMode.SURVIVAL);
                p.sendMessage("You have joined the" + MinecraftManhuntPlugin.RUNNERS_COLOR + " Runners team.");
            }
            case ELIMINATED -> {
                eliminated.addEntry(p.getName());
                p.setGameMode(GameMode.SURVIVAL);
                p.sendMessage("You have been" + MinecraftManhuntPlugin.ELIMINATED_COLOR + " eliminated.");
            }
            case HUNTERS -> {
                hunters.addEntry(p.getName());
                p.setGameMode(GameMode.SURVIVAL);
                p.sendMessage("You have joined the" + MinecraftManhuntPlugin.HUNTERS_COLOR + " Hunters team.");
            }
            case SPECTATORS -> {
                spectators.addEntry(p.getName());
                p.setGameMode(GameMode.SPECTATOR);
                p.sendMessage("You have joined the" + MinecraftManhuntPlugin.SPECTATORS_COLOR + " Spectators team.");
            }
        }
    }

    public static void eliminatePlayer(Player p) {
        eliminated.addEntry(p.getName());
        Bukkit.broadcastMessage(MinecraftManhuntPlugin.RUNNERS_COLOR + p.getName() + ChatColor.RESET + " has been " +
                MinecraftManhuntPlugin.HUNTERS_COLOR + "eliminated.");
    }

    public static void unEliminatePlayer(Player p) {
        runners.addEntry(p.getName());
        Bukkit.broadcastMessage(MinecraftManhuntPlugin.ELIMINATED_COLOR + p.getName() + ChatColor.RESET + " has been " +
                MinecraftManhuntPlugin.RUNNERS_COLOR + "un-eliminated and is now a Runner.");
    }
}
