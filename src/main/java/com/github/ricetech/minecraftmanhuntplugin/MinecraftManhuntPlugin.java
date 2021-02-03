package com.github.ricetech.minecraftmanhuntplugin;

import com.github.ricetech.minecraftmanhuntplugin.commands.*;
import com.github.ricetech.minecraftmanhuntplugin.data.ScoreKeeper;
import com.github.ricetech.minecraftmanhuntplugin.data.TeamManager;
import com.github.ricetech.minecraftmanhuntplugin.listeners.InventoryHandlerListener;
import com.github.ricetech.minecraftmanhuntplugin.listeners.PlayerDeathCoordsListener;
import net.md_5.bungee.api.chat.TextComponent;
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
    public static final String SELF_ELIMINATE_COMMAND_ALIAS = "selfelim";
    public static final String TEAM_TP_COMMAND_ALIAS = "teamtp";

    // Standard Colours
    public static final ChatColor RUNNERS_COLOR = ChatColor.DARK_BLUE;
    public static final ChatColor ELIMINATED_COLOR = ChatColor.DARK_AQUA;
    public static final ChatColor HUNTERS_COLOR = ChatColor.RED;
    public static final ChatColor SPECTATORS_COLOR = ChatColor.GOLD;

    // Internal variables
    private boolean gameInProgress = false;

    // Plugin Helper Components
    private ScoreKeeper scoreKeeper;
    private TeamManager teamManager;

    // Commands with public methods
    private TeamTpCommand teamTpCommand;
    private SelfEliminateCommand selfEliminateCommand;

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onEnable() {
        getLogger().info("Manhunt Plugin enabled!");

        // Init Managers
        this.scoreKeeper = new ScoreKeeper();
        this.teamManager = new TeamManager();

        // Init commands with public methods
        this.teamTpCommand = new TeamTpCommand(this);
        this.selfEliminateCommand = new SelfEliminateCommand(this);

        // Add Event Listeners
        PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new PlayerDeathCoordsListener(), this);
        manager.registerEvents(new InventoryHandlerListener(), this);

        // Register Commands
        this.getCommand(COUNTDOWN_COMMAND_ALIAS).setExecutor(new CountdownCommand(this));
        this.getCommand(RESET_COMMAND_ALIAS).setExecutor(new ResetCommand(this));
        this.getCommand(TEAM_SWITCH_COMMAND_ALIAS).setExecutor(new TeamSwitchCommand(this));
        this.getCommand(NEW_GAME_COMMAND_ALIAS).setExecutor(new NewGameCommand());
        this.getCommand(START_GAME_COMMAND_ALIAS).setExecutor(new StartGameCommand(this));
        this.getCommand(STOP_GAME_COMMAND_ALIAS).setExecutor(new StopGameCommand(this));

        // Register commands with public methods
        this.getCommand(TEAM_TP_COMMAND_ALIAS).setExecutor(teamTpCommand);
        this.getCommand(SELF_ELIMINATE_COMMAND_ALIAS).setExecutor(selfEliminateCommand);
    }

    @Override
    public void onDisable() {
        getLogger().info("Manhunt Plugin disabled!");
    }

    public static void sendOnlyPlayersErrorMsg(CommandSender target) {
        TextComponent errorMsg = new TextComponent("Error: Only players can use this command.");
        errorMsg.setColor(net.md_5.bungee.api.ChatColor.RED);
        target.spigot().sendMessage(errorMsg);
    }

    public ScoreKeeper getScoreKeeper() {
        return scoreKeeper;
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }

    public TeamTpCommand getTeamTpCommand() {
        return teamTpCommand;
    }

    public SelfEliminateCommand getSelfEliminateCommand() {
        return selfEliminateCommand;
    }

    public boolean isGameInProgress() {
        return gameInProgress;
    }

    public void setGameInProgress(boolean gameInProgress) {
        this.gameInProgress = gameInProgress;
    }
}
