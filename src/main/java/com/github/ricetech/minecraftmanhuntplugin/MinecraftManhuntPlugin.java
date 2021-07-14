package com.github.ricetech.minecraftmanhuntplugin;

import com.github.ricetech.minecraftmanhuntplugin.commands.*;
import com.github.ricetech.minecraftmanhuntplugin.data.ManhuntTeam;
import com.github.ricetech.minecraftmanhuntplugin.data.ScoreKeeper;
import com.github.ricetech.minecraftmanhuntplugin.data.TeamManager;
import com.github.ricetech.minecraftmanhuntplugin.listeners.InventoryHandlerListener;
import com.github.ricetech.minecraftmanhuntplugin.listeners.PlayerDeathCauseListener;
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
    public static final String TEAM_TP_COMMAND_ALIAS = "teamtp";
    public static final String SELF_ELIMINATE_COMMAND_ALIAS = "selfelim";
    public static final String TP_OPTIONS_COMMAND_ALIAS = "tpoptions";

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

    // Plugin Helper Components
    private ScoreKeeper scoreKeeper;
    private TeamManager teamManager;

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onEnable() {
        getLogger().info("Manhunt Plugin enabled!");

        // Init Managers
        this.scoreKeeper = new ScoreKeeper();
        this.teamManager = new TeamManager();

        // Add Event Listeners
        PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new PlayerDeathCoordsListener(), this);
        manager.registerEvents(new InventoryHandlerListener(), this);
        manager.registerEvents(new PlayerDeathCauseListener(this), this);

        // Register Commands
        this.getCommand(COUNTDOWN_COMMAND_ALIAS).setExecutor(new CountdownCommand(this));
        this.getCommand(RESET_COMMAND_ALIAS).setExecutor(new ResetCommand(this));
        this.getCommand(TEAM_SWITCH_COMMAND_ALIAS).setExecutor(new TeamSwitchCommand(this));
        this.getCommand(NEW_GAME_COMMAND_ALIAS).setExecutor(new NewGameCommand(this));
        this.getCommand(START_GAME_COMMAND_ALIAS).setExecutor(new StartGameCommand(this));
        this.getCommand(STOP_GAME_COMMAND_ALIAS).setExecutor(new StopGameCommand(this));
        this.getCommand(TEAM_TP_COMMAND_ALIAS).setExecutor(new TeamTpCommand(this));
        this.getCommand(SELF_ELIMINATE_COMMAND_ALIAS).setExecutor(new SelfEliminateCommand(this));
        this.getCommand(TP_OPTIONS_COMMAND_ALIAS).setExecutor(new TpOptionsCommand(this));
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

    public static ChatColor getBukkitTeamColor(ManhuntTeam team) {
        switch (team) {
            case RUNNERS:
                return RUNNERS_COLOR;
            case ELIMINATED:
                return ELIMINATED_COLOR;
            case HUNTERS:
                return HUNTERS_COLOR;
            case SPECTATORS:
                return SPECTATORS_COLOR;
            default:
                return ChatColor.RESET;
        }
    }

    public static net.md_5.bungee.api.ChatColor getBungeeCordTeamColor(ManhuntTeam team) {
        switch (team) {
            case RUNNERS:
                return RUNNERS_COLOR_BUNGEE;
            case ELIMINATED:
                return ELIMINATED_COLOR_BUNGEE;
            case HUNTERS:
                return HUNTERS_COLOR_BUNGEE;
            case SPECTATORS:
                return SPECTATORS_COLOR_BUNGEE;
            default:
                return net.md_5.bungee.api.ChatColor.RESET;
        }
    }

    public ScoreKeeper getScoreKeeper() {
        return scoreKeeper;
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }

    public boolean isGameInProgress() {
        return gameInProgress;
    }

    public void setGameInProgress(boolean gameInProgress) {
        this.gameInProgress = gameInProgress;
    }
}
