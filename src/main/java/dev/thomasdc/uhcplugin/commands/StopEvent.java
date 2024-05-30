package dev.thomasdc.uhcplugin.commands;

import dev.thomasdc.uhcplugin.UHCPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StopEvent implements CommandExecutor {
    private final UHCPlugin plugin;

    public StopEvent(UHCPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (command.getName().equals("stopEvent")) {
                Player p = (Player) sender;
                if (UHCPlugin.eventActive) UHCPlugin.eventActive = false;
            }
        }

        return true;
    }
}
