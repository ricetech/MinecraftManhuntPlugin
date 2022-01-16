package com.github.ricetech.minecraftmanhuntplugin.commands.utility;

import com.github.ricetech.minecraftmanhuntplugin.MinecraftManhuntPlugin;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class VersionCommand implements CommandExecutor {
    private final MinecraftManhuntPlugin manhuntPlugin;

    public VersionCommand(MinecraftManhuntPlugin manhuntPlugin){
        this.manhuntPlugin = manhuntPlugin;
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length != 0) {
            return false;
        }

        String version = this.manhuntPlugin.getDescription().getVersion();

        sender.sendMessage(MinecraftManhuntPlugin.GAME_MSG_COLOR + "Minecraft Manhunt Plugin v" + version);

        ComponentBuilder builder = new ComponentBuilder(MinecraftManhuntPlugin.GAME_MSG_COLOR_BUNGEE + "Plugin website: ");
        TextComponent websiteLink = new TextComponent("https://github.com/ricetech/MinecraftManhuntPlugin");
        websiteLink.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/ricetech/MinecraftManhuntPlugin"));
        websiteLink.setColor(MinecraftManhuntPlugin.GAME_MSG_COLOR_BUNGEE);
        websiteLink.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Open the Plugin website")));

        builder.append(websiteLink);

        sender.spigot().sendMessage(builder.create());
        return true;
    }
}
