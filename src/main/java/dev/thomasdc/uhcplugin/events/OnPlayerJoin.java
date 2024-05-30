package dev.thomasdc.uhcplugin.events;

import dev.thomasdc.uhcplugin.UHCPlugin;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;

@RequiredArgsConstructor
public class OnPlayerJoin implements Listener {
    private final UHCPlugin plugin;
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if(plugin.leaderboard.getString(player.getUniqueId().toString()) == null){
            plugin.leaderboard.set(player.getUniqueId().toString(), 0);
            try{
                plugin.leaderboard.save(new File(plugin.getDataFolder(), "leaderboard.yml"));
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }

    }
}
