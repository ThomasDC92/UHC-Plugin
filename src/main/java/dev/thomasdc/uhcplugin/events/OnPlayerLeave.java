package dev.thomasdc.uhcplugin.events;

import dev.thomasdc.uhcplugin.UHCPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnPlayerLeave implements Listener {

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        if(UHCPlugin.eventPlayers.containsKey(event.getPlayer()))
            UHCPlugin.eventPlayers.remove(event.getPlayer());

        if(UHCPlugin.alivePlayers.contains(event.getPlayer()))
            UHCPlugin.alivePlayers.remove(event.getPlayer());

        if(UHCPlugin.noFallDamage.contains(event.getPlayer()))
            UHCPlugin.noFallDamage.remove(event.getPlayer());
    }
}
