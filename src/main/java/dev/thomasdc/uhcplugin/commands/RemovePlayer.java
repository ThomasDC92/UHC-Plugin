package dev.thomasdc.uhcplugin.commands;

import dev.thomasdc.uhcplugin.UHCPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemovePlayer implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (command.getName().equals("removePlayer")) {
                if (!sender.isOp()) {
                    sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
                    return true;
                }
                if (args.length == 0) {
                    sender.sendMessage(ChatColor.RED + "Please specify a player to remove!");
                    return true;
                }
                Player p = (Player) sender;
                Player target = p.getServer().getPlayer(args[0]);
                if (target == null) {
                    p.sendMessage(ChatColor.RED + "Player not found!");
                    return true;
                }
                if (UHCPlugin.eventPlayers.containsKey(target)) {
                    UHCPlugin.eventPlayers.remove(target);
                    target.sendMessage(ChatColor.RED + "You have been removed from the event!");
                } else {
                    p.sendMessage(ChatColor.RED + "Player is not in the event!");
                }
                return true;

            }
        }

        return true;
    }
}
