package dev.thomasdc.uhcplugin.events;

import dev.thomasdc.uhcplugin.UHCPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnPlayerLeave implements Listener {

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        UHCPlugin.eventPlayers.remove(event.getPlayer());
    }
}
