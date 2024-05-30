package dev.thomasdc.uhcplugin.events;

import dev.thomasdc.uhcplugin.UHCPlugin;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class OnPlayerDeath implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player p = event.getEntity();
        if (UHCPlugin.alivePlayers.contains(p)) {
            UHCPlugin.alivePlayers.remove(p);
            p.setGameMode(GameMode.SPECTATOR);
            System.out.println("Player " + p.getName() + " died!");
            System.out.println("Alive players: " + UHCPlugin.alivePlayers.size());
        }
    }
}
