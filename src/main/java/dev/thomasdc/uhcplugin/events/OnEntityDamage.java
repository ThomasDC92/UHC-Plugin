package dev.thomasdc.uhcplugin.events;

import dev.thomasdc.uhcplugin.UHCPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class OnEntityDamage implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player){
            Player p = (Player) event.getEntity();
            if(UHCPlugin.noFallDamage.contains(p)){
                UHCPlugin.noFallDamage.remove(p);
                event.setCancelled(true);
            }
        }
    }
}
