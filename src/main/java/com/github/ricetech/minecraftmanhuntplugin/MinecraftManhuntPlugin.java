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
    public static final String COUNTDOWN_COMMAND_ALIAS = "countdown";
    public static final String RESET_COMMAND_ALIAS = "rs";
    public static final String TEAM_SWITCH_COMMAND_ALIAS = "changeteam";
    public static final String NEW_GAME_COMMAND_ALIAS = "newgame";
    public static final String START_GAME_COMMAND_ALIAS = "startgame";
    public static final String STOP_GAME_COMMAND_ALIAS = "stopgame";
    public static final String SELF_ELIMINATE_COMMAND_ALIAS = "selfelim";

    public static final ChatColor RUNNERS_COLOR = ChatColor.DARK_BLUE;
    public static final ChatColor ELIMINATED_COLOR = ChatColor.DARK_AQUA;
    public static final ChatColor HUNTERS_COLOR = ChatColor.RED;
    public static final ChatColor SPECTATORS_COLOR = ChatColor.GOLD;

    private boolean gameInProgress = false;

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

        // Add Commands
        this.getCommand(COUNTDOWN_COMMAND_ALIAS).setExecutor(new CountdownCommand(this));
        this.getCommand(RESET_COMMAND_ALIAS).setExecutor(new ResetCommand(this.scoreKeeper, this.teamManager));
        this.getCommand(TEAM_SWITCH_COMMAND_ALIAS).setExecutor(new TeamSwitchCommand(this.teamManager));
        this.getCommand(NEW_GAME_COMMAND_ALIAS).setExecutor(new NewGameCommand());
        this.getCommand(START_GAME_COMMAND_ALIAS).setExecutor(new StartGameCommand(this));
        this.getCommand(STOP_GAME_COMMAND_ALIAS).setExecutor(new StopGameCommand(this));
        this.getCommand(SELF_ELIMINATE_COMMAND_ALIAS).setExecutor(new SelfEliminateCommand(this.teamManager));
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

    public boolean isGameInProgress() {
        return gameInProgress;
    }

    public void setGameInProgress(boolean gameInProgress) {
        this.gameInProgress = gameInProgress;
    }
}
