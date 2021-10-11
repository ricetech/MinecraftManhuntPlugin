package com.github.ricetech.minecraftmanhuntplugin.commands.player;

import com.github.ricetech.minecraftmanhuntplugin.MinecraftManhuntPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TrackPortalCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length != 0) {
            return false;
        }

        if (!(sender instanceof Player p)) {
            MinecraftManhuntPlugin.sendOnlyPlayersErrorMsg(sender);
            return true;
        }

        TrackCommand.putTrackingEntry(p.getName(), TrackCommand.PORTAL_NAME_KEY);
        TrackCommand.trackPortal(p);
        return true;
    }
}
