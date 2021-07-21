package com.github.ricetech.minecraftmanhuntplugin.commands.internal;

import com.github.ricetech.minecraftmanhuntplugin.MinecraftManhuntPlugin;
import com.github.ricetech.minecraftmanhuntplugin.commands.player.TeamSwitchCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ResetEligibilityCommand implements CommandExecutor {
    @SuppressWarnings("FieldCanBeLocal")
    private final MinecraftManhuntPlugin manhuntPlugin;

    public ResetEligibilityCommand(MinecraftManhuntPlugin manhuntPlugin) {
        this.manhuntPlugin = manhuntPlugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length != 0) {
            return false;
        }

        Player p;

        // Ensure sender is a player, then cast sender to Player
        if (!(sender instanceof Player)) {
            MinecraftManhuntPlugin.sendOnlyPlayersErrorMsg(sender);
            return true;
        } else {
            p = ((Player) sender);
        }

        TeamTpCommand.setEligibility(p.getName(), false);
        SelfEliminateCommand.setEligibility(p.getName(), false);
        TeamSwitchCommand.setEligibility(p.getName(), false);

        p.sendMessage(ChatColor.GREEN + "Response received.");

        return true;
    }
}
