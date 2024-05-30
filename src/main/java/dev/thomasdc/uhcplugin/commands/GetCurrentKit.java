package dev.thomasdc.uhcplugin.commands;

import dev.thomasdc.uhcplugin.UHCPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetCurrentKit implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            if(command.getName().equals("getCurrentKit")){
                Player p = (Player) sender;
                if(UHCPlugin.eventPlayers.containsKey(p)){
                    p.sendMessage("You have selected the " + UHCPlugin.eventPlayers.get(p).getName() + " kit");
                }else{
                    p.sendMessage("You have not selected a kit yet");
                }
            }
        }
        return true;
    }
}
