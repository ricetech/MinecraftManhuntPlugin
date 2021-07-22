package com.github.ricetech.minecraftmanhuntplugin.commands.internal;

import com.github.ricetech.minecraftmanhuntplugin.MinecraftManhuntPlugin;
import com.github.ricetech.minecraftmanhuntplugin.data.TeamManager;
import com.github.ricetech.minecraftmanhuntplugin.listeners.PlayerDeathLocationStorageListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class TeamTpCommand implements CommandExecutor {
    public static final String SELF_TP_ARG = "$elf";

    private static final long SAFETY_DELAY_SECONDS = 5L;
    private static final int INVINCIBILITY_DURATION_SECONDS = 25;

    private static final Map<String, Boolean> eligibility = new HashMap<>();

    private final MinecraftManhuntPlugin manhuntPlugin;

    public TeamTpCommand(MinecraftManhuntPlugin manhuntPlugin) {
        this.manhuntPlugin = manhuntPlugin;
    }

    public static boolean getEligibility(String entry) {
        return eligibility.getOrDefault(entry, false);
    }

    public static void setEligibility(String entry, boolean isEligible) {
        eligibility.put(entry, isEligible);
    }

    public static void reset() {
        eligibility.clear();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        Player p;

        if (args.length != 1) {
            return false;
        }

        // Ensure sender is a player, then cast sender to Player
        if (!(sender instanceof Player)) {
            MinecraftManhuntPlugin.sendOnlyPlayersErrorMsg(sender);
            return true;
        } else {
            p = ((Player) sender);
        }

        if (args[0].equals(SELF_TP_ARG)) { // Teleport to last death location
            // Attempt to get last death location
            Location deathLoc = PlayerDeathLocationStorageListener.getLatestDeathLoc(p);

            if (deathLoc == null) {
               MinecraftManhuntPlugin.sendErrorMsg(sender, "You do not have a valid death location saved.");
               eligibility.put(p.getName(), false);
               return true;
            }

            eligibility.put(p.getName(), false);
            // Disable eligibility for /selfelim
            SelfEliminateCommand.setEligibility(p.getName(), false);

            // Make player invincible for 25 seconds
            p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, INVINCIBILITY_DURATION_SECONDS, 255));

            // Announce TP
            Bukkit.broadcastMessage(MinecraftManhuntPlugin.RUNNERS_COLOR + p.getName() + "is teleporting to their " +
                    "death location and will be" + ChatColor.RED + " invincible " + ChatColor.RESET + "for 25 seconds.");

            // Teleport to death location
            p.teleport(deathLoc);
        } else { // Teleport to teammate
            // Get Player object of target player, check if online
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                MinecraftManhuntPlugin.sendErrorMsg(sender, "Target player does not exist.");
                Bukkit.dispatchCommand(p, MinecraftManhuntPlugin.TP_OPTIONS_COMMAND_ALIAS);
                return true;
            }

            if (TeamManager.getTeam(p) != TeamManager.getTeam(target)) {
                MinecraftManhuntPlugin.sendErrorMsg(sender, "Target player is not on your team. Try clicking on another player.");
                Bukkit.dispatchCommand(p, MinecraftManhuntPlugin.TP_OPTIONS_COMMAND_ALIAS);
                return true;
            }

            if (!eligibility.getOrDefault(p.getName(), false)) {
                MinecraftManhuntPlugin.sendErrorMsg(sender, "You are not eligible to teleport.");
                return true;
            }

            eligibility.put(p.getName(), false);
            // Disable eligibility for /selfelim
            SelfEliminateCommand.setEligibility(p.getName(), false);

            new TeamTpRunnable(p, target).runTaskLater(this.manhuntPlugin, SAFETY_DELAY_SECONDS * 20L);

            // Alert players
            String secondsWord = SAFETY_DELAY_SECONDS == 1 ? " second" : " seconds";

            p.sendMessage(MinecraftManhuntPlugin.WARNING_MSG_COLOR + "Alert: You will be teleported to " + target.getName() + " in " +
                    SAFETY_DELAY_SECONDS + secondsWord + ".");
            target.sendMessage(MinecraftManhuntPlugin.WARNING_MSG_COLOR + "Alert: " + p.getName() + " will be teleported to you in " +
                    SAFETY_DELAY_SECONDS + secondsWord + ". Make sure you are in a safe location!");
        }

        return true;
    }

    private static class TeamTpRunnable extends BukkitRunnable {
        private final Player p;
        private final Player target;

        public TeamTpRunnable(Player p, Player target) {
            this.p = p;
            this.target = target;
        }

        @Override
        public void run() {
            p.teleport(target);
        }
    }
}
