package com.github.ricetech.minecraftmanhuntplugin;

import com.github.ricetech.minecraftmanhuntplugin.commands.game.*;
import com.github.ricetech.minecraftmanhuntplugin.commands.internal.ResetEligibilityCommand;
import com.github.ricetech.minecraftmanhuntplugin.commands.internal.SelfEliminateCommand;
import com.github.ricetech.minecraftmanhuntplugin.commands.internal.TeamTpCommand;
import com.github.ricetech.minecraftmanhuntplugin.commands.internal.TpOptionsCommand;
import com.github.ricetech.minecraftmanhuntplugin.commands.op.*;
import com.github.ricetech.minecraftmanhuntplugin.commands.player.TeamSwitchCommand;
import com.github.ricetech.minecraftmanhuntplugin.commands.player.TrackCommand;
import com.github.ricetech.minecraftmanhuntplugin.commands.player.TrackPortalCommand;
import com.github.ricetech.minecraftmanhuntplugin.commands.utility.CountdownCommand;
import com.github.ricetech.minecraftmanhuntplugin.commands.utility.ResetCommand;
import com.github.ricetech.minecraftmanhuntplugin.commands.utility.VersionCommand;
import com.github.ricetech.minecraftmanhuntplugin.data.ManhuntTeam;
import com.github.ricetech.minecraftmanhuntplugin.data.ScoreKeeper;
import com.github.ricetech.minecraftmanhuntplugin.data.TeamManager;
import com.github.ricetech.minecraftmanhuntplugin.listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings({"unused", "DuplicatedCode"})
public class MinecraftManhuntPlugin extends JavaPlugin {
    // Command Aliases
    public static final String VERSION_COMMAND_ALIAS = "manhuntversion";
    // Game Management
    public static final String CLEAR_TEAMS_COMMAND_ALIAS = "clearteams";
    public static final String MAKE_TEAMS_COMMAND_ALIAS = "maketeams";
    public static final String START_GAME_COMMAND_ALIAS = "startgame";
    public static final String STOP_GAME_COMMAND_ALIAS = "stopgame";
    // Utilities
    public static final String COUNTDOWN_COMMAND_ALIAS = "countdown";
    public static final String RESET_COMMAND_ALIAS = "reset";
    // Player
    public static final String LIST_TEAMS_COMMAND_ALIAS = "listteams";
    public static final String TRACK_COMMAND_ALIAS = "track";
    public static final String TRACK_PORTAL_COMMAND_ALIAS = "trackportal";
    // Operator
    public static final String CREDIT_KILL_COMMAND_ALIAS = "creditkill";
    public static final String ELIMINATE_COMMAND_ALIAS = "eliminate";
    public static final String OFFER_TEAM_TP_COMMAND_ALIAS = "offerteamtp";
    public static final String REMOVE_DEATH_COMMAND_ALIAS = "removedeath";
    public static final String SET_PLAYER_TEAM_COMMAND_ALIAS = "setplayerteam";
    public static final String UN_ELIMINATE_COMMAND_ALIAS = "uneliminate";
    // Internal Use
    public static final String TEAM_SWITCH_COMMAND_ALIAS = "changeteam";
    public static final String RESET_ELIGIBILITY_COMMAND_ALIAS = "reseteligibility";
    public static final String SELF_ELIMINATE_COMMAND_ALIAS = "selfelim";
    public static final String TEAM_TP_COMMAND_ALIAS = "teamtp";
    public static final String TP_OPTIONS_COMMAND_ALIAS = "tpoptions";

    // Standard Colours
    public static final ChatColor RUNNERS_COLOR = ChatColor.DARK_BLUE;
    public static final ChatColor ELIMINATED_COLOR = ChatColor.DARK_AQUA;
    public static final ChatColor HUNTERS_COLOR = ChatColor.RED;
    public static final ChatColor SPECTATORS_COLOR = ChatColor.GOLD;

    public static final ChatColor GAME_MSG_COLOR = ChatColor.AQUA;
    public static final ChatColor WARNING_MSG_COLOR = ChatColor.YELLOW;
    public static final ChatColor WHISPER_MSG_COLOR = ChatColor.GRAY;

    // Bungee Colours (Keep in sync with the colours above!)
    public static final net.md_5.bungee.api.ChatColor RUNNERS_COLOR_BUNGEE = net.md_5.bungee.api.ChatColor.DARK_BLUE;
    public static final net.md_5.bungee.api.ChatColor ELIMINATED_COLOR_BUNGEE = net.md_5.bungee.api.ChatColor.DARK_AQUA;
    public static final net.md_5.bungee.api.ChatColor HUNTERS_COLOR_BUNGEE = net.md_5.bungee.api.ChatColor.RED;
    public static final net.md_5.bungee.api.ChatColor SPECTATORS_COLOR_BUNGEE = net.md_5.bungee.api.ChatColor.GOLD;

    public static final net.md_5.bungee.api.ChatColor GAME_MSG_COLOR_BUNGEE = net.md_5.bungee.api.ChatColor.AQUA;
    public static final net.md_5.bungee.api.ChatColor WARNING_MSG_COLOR_BUNGEE = net.md_5.bungee.api.ChatColor.YELLOW;
    public static final net.md_5.bungee.api.ChatColor WHISPER_MSG_COLOR_BUNGEE = net.md_5.bungee.api.ChatColor.GRAY;

    // Title settings
    public static final int TITLE_FADE_IN = 10;
    public static final int TITLE_STAY = 70;
    public static final int TITLE_FADE_OUT = 20;

    // Internal variables
    public static boolean isGameInProgress = false;
    public static boolean isTeamSelectInProgress = false;

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onEnable() {
        getLogger().info("Manhunt Plugin enabled!");

        // Init Managers
        ScoreKeeper.init();
        TeamManager.init();

        // Add Event Listeners
        PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new AddPlayerToScoreboardListener(), this);
        manager.registerEvents(new SendTeamSelectOnJoin(), this);
        manager.registerEvents(new CompassInventoryHandlerListener(), this);
        manager.registerEvents(new CompassTrackListener(), this);
        manager.registerEvents(new FireResistanceOnPortalListener(), this);
        manager.registerEvents(new NightVisionListener(this), this);
        manager.registerEvents(new PlayerDeathCoordsListener(), this);
        manager.registerEvents(new PlayerDeathLocationStorageListener(), this);
        manager.registerEvents(new PlayerDisconnectLocationStorageListener(this), this);
        manager.registerEvents(new PlayerEliminationOnDeathListener(), this);
        manager.registerEvents(new PlayerPortalLocationStorageListener(this), this);

        // Register Commands
        // Game Management
        this.getCommand(CLEAR_TEAMS_COMMAND_ALIAS).setExecutor(new ClearTeamsCommand());
        this.getCommand(MAKE_TEAMS_COMMAND_ALIAS).setExecutor(new MakeTeamsCommand());
        this.getCommand(START_GAME_COMMAND_ALIAS).setExecutor(new StartGameCommand(this));
        this.getCommand(STOP_GAME_COMMAND_ALIAS).setExecutor(new StopGameCommand());
        // Utilities
        this.getCommand(COUNTDOWN_COMMAND_ALIAS).setExecutor(new CountdownCommand(this));
        this.getCommand(RESET_COMMAND_ALIAS).setExecutor(new ResetCommand(this));
        this.getCommand(VERSION_COMMAND_ALIAS).setExecutor(new VersionCommand(this));
        // Player
        this.getCommand(LIST_TEAMS_COMMAND_ALIAS).setExecutor(new ListTeamsCommand());
        this.getCommand(TRACK_COMMAND_ALIAS).setExecutor(new TrackCommand());
        this.getCommand(TRACK_PORTAL_COMMAND_ALIAS).setExecutor(new TrackPortalCommand());
        // Operator
        this.getCommand(CREDIT_KILL_COMMAND_ALIAS).setExecutor(new CreditKillCommand());
        this.getCommand(ELIMINATE_COMMAND_ALIAS).setExecutor(new EliminatePlayerCommand());
        this.getCommand(OFFER_TEAM_TP_COMMAND_ALIAS).setExecutor(new OfferTeamTpCommand());
        this.getCommand(REMOVE_DEATH_COMMAND_ALIAS).setExecutor(new RemoveDeathCommand());
        this.getCommand(SET_PLAYER_TEAM_COMMAND_ALIAS).setExecutor(new SetPlayerTeamCommand());
        this.getCommand(UN_ELIMINATE_COMMAND_ALIAS).setExecutor(new UnEliminatePlayerCommand());
        // Internal Use
        this.getCommand(TEAM_SWITCH_COMMAND_ALIAS).setExecutor(new TeamSwitchCommand());
        this.getCommand(RESET_ELIGIBILITY_COMMAND_ALIAS).setExecutor(new ResetEligibilityCommand());
        this.getCommand(SELF_ELIMINATE_COMMAND_ALIAS).setExecutor(new SelfEliminateCommand());
        this.getCommand(TEAM_TP_COMMAND_ALIAS).setExecutor(new TeamTpCommand(this));
        this.getCommand(TP_OPTIONS_COMMAND_ALIAS).setExecutor(new TpOptionsCommand());

        // Set Tab Completers
        // Game Management
        this.getCommand(CLEAR_TEAMS_COMMAND_ALIAS).setTabCompleter(new ManhuntTabCompleter());
        this.getCommand(MAKE_TEAMS_COMMAND_ALIAS).setTabCompleter(new ManhuntTabCompleter());
        this.getCommand(START_GAME_COMMAND_ALIAS).setTabCompleter(new ManhuntTabCompleter());
        this.getCommand(STOP_GAME_COMMAND_ALIAS).setTabCompleter(new ManhuntTabCompleter());
        // Utilities
        this.getCommand(COUNTDOWN_COMMAND_ALIAS).setTabCompleter(new ManhuntTabCompleter());
        this.getCommand(RESET_COMMAND_ALIAS).setTabCompleter(new ManhuntTabCompleter());
        this.getCommand(VERSION_COMMAND_ALIAS).setTabCompleter(new ManhuntTabCompleter());
        // Player
        this.getCommand(LIST_TEAMS_COMMAND_ALIAS).setTabCompleter(new ManhuntTabCompleter());
        this.getCommand(TRACK_COMMAND_ALIAS).setTabCompleter(new ManhuntTabCompleter());
        this.getCommand(TRACK_PORTAL_COMMAND_ALIAS).setTabCompleter(new ManhuntTabCompleter());
        // Operator
        this.getCommand(CREDIT_KILL_COMMAND_ALIAS).setTabCompleter(new ManhuntTabCompleter());
        this.getCommand(ELIMINATE_COMMAND_ALIAS).setTabCompleter(new ManhuntTabCompleter());
        this.getCommand(OFFER_TEAM_TP_COMMAND_ALIAS).setTabCompleter(new ManhuntTabCompleter());
        this.getCommand(REMOVE_DEATH_COMMAND_ALIAS).setTabCompleter(new ManhuntTabCompleter());
        this.getCommand(SET_PLAYER_TEAM_COMMAND_ALIAS).setTabCompleter(new ManhuntTabCompleter());
        this.getCommand(UN_ELIMINATE_COMMAND_ALIAS).setTabCompleter(new ManhuntTabCompleter());
        // Internal Use
        this.getCommand(TEAM_SWITCH_COMMAND_ALIAS).setTabCompleter(new ManhuntTabCompleter());
        this.getCommand(RESET_ELIGIBILITY_COMMAND_ALIAS).setTabCompleter(new ManhuntTabCompleter());
        this.getCommand(SELF_ELIMINATE_COMMAND_ALIAS).setTabCompleter(new ManhuntTabCompleter());
        this.getCommand(TEAM_TP_COMMAND_ALIAS).setTabCompleter(new ManhuntTabCompleter());
        this.getCommand(TP_OPTIONS_COMMAND_ALIAS).setTabCompleter(new ManhuntTabCompleter());

        // Stop time
        World overworld = Bukkit.getServer().getWorlds().getFirst();
        overworld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        overworld.setTime(6000L);
    }

    @Override
    public void onDisable() {
        getLogger().info("Manhunt Plugin disabled!");
    }

    public static void sendOnlyPlayersErrorMsg(CommandSender target) {
        sendErrorMsg(target, "Only players can use this command.");
    }

    public static void sendErrorMsg(CommandSender target, String message) {
        target.sendMessage(ChatColor.RED + "Error: " + message);
    }

    public static ChatColor getBukkitTeamColor(ManhuntTeam team) {
        if (team != null) {
            return switch (team) {
                case RUNNERS -> RUNNERS_COLOR;
                case ELIMINATED -> ELIMINATED_COLOR;
                case HUNTERS -> HUNTERS_COLOR;
                case SPECTATORS -> SPECTATORS_COLOR;
            };
        } else {
            return ChatColor.RESET;
        }
    }

    public static net.md_5.bungee.api.ChatColor getBungeeCordTeamColor(ManhuntTeam team) {
        if (team != null) {
            return switch (team) {
                case RUNNERS -> RUNNERS_COLOR_BUNGEE;
                case ELIMINATED -> ELIMINATED_COLOR_BUNGEE;
                case HUNTERS -> HUNTERS_COLOR_BUNGEE;
                case SPECTATORS -> SPECTATORS_COLOR_BUNGEE;
            };
        } else {
            return net.md_5.bungee.api.ChatColor.RESET;
        }
    }

    public static String getDimensionName(World.Environment e) {
        return switch (e) {
            case NORMAL -> "Overworld";
            case NETHER -> "The Nether";
            case THE_END -> "The End";
            case CUSTOM -> "Custom World (You should not be seeing this)";
        };
    }
}
