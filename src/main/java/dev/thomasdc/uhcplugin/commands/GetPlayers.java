package dev.thomasdc.uhcplugin.commands;

import dev.thomasdc.uhcplugin.UHCPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetPlayers implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            if(command.getName().equals("getPlayers")){
                Player p = (Player) sender;
                String output = "Players in the event:\n";
                System.out.println(UHCPlugin.eventPlayers.keySet());
                for(Player player : UHCPlugin.eventPlayers.keySet()){
                    output += player.getName() + ",";
                }
                p.sendMessage(output);
            }
        }

        return true;
    }
}
