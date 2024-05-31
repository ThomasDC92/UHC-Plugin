package dev.thomasdc.uhcplugin.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class OnWeatherChange implements Listener {
    @EventHandler
    public void onWeatherChange(WeatherChangeEvent e) {
        e.setCancelled(true);
    }
}
