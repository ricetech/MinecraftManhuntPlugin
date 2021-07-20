package com.github.ricetech.minecraftmanhuntplugin;

import com.github.ricetech.minecraftmanhuntplugin.commands.*;
import com.github.ricetech.minecraftmanhuntplugin.data.ManhuntTeam;
import com.github.ricetech.minecraftmanhuntplugin.data.ScoreKeeper;
import com.github.ricetech.minecraftmanhuntplugin.data.TeamManager;
import com.github.ricetech.minecraftmanhuntplugin.listeners.*;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class MinecraftManhuntPlugin extends JavaPlugin {
    // Command Aliases
    public static final String COUNTDOWN_COMMAND_ALIAS = "countdown";
    public static final String RESET_COMMAND_ALIAS = "rs";
    public static final String TEAM_SWITCH_COMMAND_ALIAS = "changeteam";
    public static final String NEW_GAME_COMMAND_ALIAS = "newgame";
    public static final String START_GAME_COMMAND_ALIAS = "startgame";
    public static final String STOP_GAME_COMMAND_ALIAS = "stopgame";
    public static final String TEAM_TP_COMMAND_ALIAS = "teamtp";
    public static final String SELF_ELIMINATE_COMMAND_ALIAS = "selfelim";
    public static final String TP_OPTIONS_COMMAND_ALIAS = "tpoptions";
    public static final String RESET_ELIGIBILITY_COMMAND_ALIAS = "reseteligibility";
    public static final String TRACK_COMMAND_ALIAS = "trackn";

    // Standard Colours
    public static final ChatColor RUNNERS_COLOR = ChatColor.DARK_BLUE;
    public static final ChatColor ELIMINATED_COLOR = ChatColor.DARK_AQUA;
    public static final ChatColor HUNTERS_COLOR = ChatColor.RED;
    public static final ChatColor SPECTATORS_COLOR = ChatColor.GOLD;

    // Bungee Colours (Keep in sync with the colours above!)
    public static final net.md_5.bungee.api.ChatColor RUNNERS_COLOR_BUNGEE = net.md_5.bungee.api.ChatColor.DARK_BLUE;
    public static final net.md_5.bungee.api.ChatColor ELIMINATED_COLOR_BUNGEE = net.md_5.bungee.api.ChatColor.DARK_AQUA;
    public static final net.md_5.bungee.api.ChatColor HUNTERS_COLOR_BUNGEE = net.md_5.bungee.api.ChatColor.RED;
    public static final net.md_5.bungee.api.ChatColor SPECTATORS_COLOR_BUNGEE = net.md_5.bungee.api.ChatColor.GOLD;

    // Internal variables
    private boolean gameInProgress = false;

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onEnable() {
        getLogger().info("Manhunt Plugin enabled!");

        // Init Managers
        ScoreKeeper.init();
        TeamManager.init();

        // Add Event Listeners
        PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new PlayerDeathCoordsListener(), this);
        manager.registerEvents(new CompassInventoryHandlerListener(), this);
        manager.registerEvents(new PlayerEliminationOnDeathListener(this), this);
        manager.registerEvents(new PlayerDeathLocationStorageListener(this), this);
        manager.registerEvents(new PlayerPortalLocationStorageListener(this), this);
        manager.registerEvents(new PlayerDisconnectLocationStorageListener(this), this);
        manager.registerEvents(new CompassTrackListener(), this);

        // Register Commands
        this.getCommand(COUNTDOWN_COMMAND_ALIAS).setExecutor(new CountdownCommand(this));
        this.getCommand(RESET_COMMAND_ALIAS).setExecutor(new ResetCommand());
        this.getCommand(TEAM_SWITCH_COMMAND_ALIAS).setExecutor(new TeamSwitchCommand(this));
        this.getCommand(NEW_GAME_COMMAND_ALIAS).setExecutor(new NewGameCommand(this));
        this.getCommand(START_GAME_COMMAND_ALIAS).setExecutor(new StartGameCommand(this));
        this.getCommand(STOP_GAME_COMMAND_ALIAS).setExecutor(new StopGameCommand(this));
        this.getCommand(TEAM_TP_COMMAND_ALIAS).setExecutor(new TeamTpCommand(this));
        this.getCommand(SELF_ELIMINATE_COMMAND_ALIAS).setExecutor(new SelfEliminateCommand());
        this.getCommand(TP_OPTIONS_COMMAND_ALIAS).setExecutor(new TpOptionsCommand());
        this.getCommand(RESET_ELIGIBILITY_COMMAND_ALIAS).setExecutor(new ResetEligibilityCommand(this));
        this.getCommand(TRACK_COMMAND_ALIAS).setExecutor(new TrackCommand());
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
        return switch (team) {
            case RUNNERS -> RUNNERS_COLOR;
            case ELIMINATED -> ELIMINATED_COLOR;
            case HUNTERS -> HUNTERS_COLOR;
            case SPECTATORS -> SPECTATORS_COLOR;
        };
    }

    public static net.md_5.bungee.api.ChatColor getBungeeCordTeamColor(ManhuntTeam team) {
        return switch (team) {
            case RUNNERS -> RUNNERS_COLOR_BUNGEE;
            case ELIMINATED -> ELIMINATED_COLOR_BUNGEE;
            case HUNTERS -> HUNTERS_COLOR_BUNGEE;
            case SPECTATORS -> SPECTATORS_COLOR_BUNGEE;
        };
    }

    public boolean isGameInProgress() {
        return gameInProgress;
    }

    public void setGameInProgress(boolean gameInProgress) {
        this.gameInProgress = gameInProgress;
    }
}
