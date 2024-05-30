package dev.thomasdc.uhcplugin.commands;

import dev.thomasdc.uhcplugin.UHCPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartEvent implements CommandExecutor {
    private final UHCPlugin plugin;

    public StartEvent(UHCPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (command.getName().equals("startEvent")) {
                Player p = (Player) sender;
                if (UHCPlugin.eventPlayers.size() > UHCPlugin.minRequiredPlayers) {
                    if (!UHCPlugin.eventActive) {
                        plugin.startEvent();
                    } else {
                        p.sendMessage(ChatColor.RED + "Event already started!");
                    }
                    p.sendMessage(ChatColor.GREEN + "Event started!");
                } else {
                    p.sendMessage(ChatColor.RED + "Not enough players to start the event!");
                }
            }
        }

        return true;
    }

}
