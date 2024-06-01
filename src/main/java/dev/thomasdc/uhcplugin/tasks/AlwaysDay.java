package dev.thomasdc.uhcplugin.tasks;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class AlwaysDay extends BukkitRunnable {
    @Override
    public void run() {
        World world = Bukkit.getWorld("uhc_world");
        if(world != null) {
            world.setTime(4000L);
        }
    }
}
