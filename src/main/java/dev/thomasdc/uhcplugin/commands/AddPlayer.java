package dev.thomasdc.uhcplugin.commands;

import dev.thomasdc.uhcplugin.UHCPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddPlayer implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            if(command.getName().equals("addPlayer")){
                if(args.length == 0){
                    sender.sendMessage(ChatColor.RED + "Please specify a player to add!");
                    return true;
                }
                Player p = (Player) sender;
                Player target = p.getServer().getPlayer(args[0]);
                if(target == null){
                    p.sendMessage(ChatColor.RED + "Player not found!");
                    return true;
                }
                if(UHCPlugin.eventPlayers.containsKey(target)){
                    p.sendMessage(ChatColor.RED + "Player is already in the event!");
                }
                else{
                    UHCPlugin.eventPlayers.put(target,null);
                    target.sendMessage(ChatColor.GREEN + "You have been added to the event!");
                }
                return true;

            }
        }

        return true;
    }
}
