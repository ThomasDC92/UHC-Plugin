package dev.thomasdc.uhcplugin.commands;

import dev.thomasdc.uhcplugin.UHCPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GoToPlayer implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (command.getName().equals("goto")) {
                Player p = (Player) sender;
                if (!UHCPlugin.alivePlayers.contains(p) && p.getGameMode().equals(GameMode.SPECTATOR)) {
                    if (args.length == 1) {
                        Player target = Bukkit.getPlayer(args[0]);
                        if (target != null) {
                            p.teleport(target);
                        } else {
                            p.sendMessage(ChatColor.RED + "Player not found!");
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "Invalid arguments!");
                    }
                }
            }
        }

        return true;
    }
}
