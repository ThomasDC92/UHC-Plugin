package dev.thomasdc.uhcplugin.commands;

import dev.thomasdc.uhcplugin.UHCPlugin;
import dev.thomasdc.uhcplugin.models.Kit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

public class EventKit implements CommandExecutor, Listener {
    private final Inventory inv;

    public EventKit() {
        inv = Bukkit.createInventory(null, 9, ChatColor.RED + "Choose a kit for the next event");

    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            if(command.getName().equals("eventKit")){
                if(UHCPlugin.eventActive){
                    sender.sendMessage("An event is already active");
                    return true;
                }
                Player p = (Player) sender;
                inv.clear();
                for (Kit kit: UHCPlugin.kits) {
                    inv.addItem(kit.getIcon());
                }
                p.openInventory(inv);
            }
        }

        return true;
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if(e.getView().getOriginalTitle().equals(ChatColor.RED + "Choose a kit for the next event")){
            e.setCancelled(true);
            if(e.getCurrentItem() == null) return;
            if(e.getCurrentItem().getItemMeta() == null) return;
            if(e.getCurrentItem().getItemMeta().getDisplayName() == null) return;
            Player p = (Player) e.getWhoClicked();
            for (Kit kit: UHCPlugin.kits) {
                if(e.getCurrentItem().getItemMeta().getDisplayName().equals(kit.getIcon().getItemMeta().getDisplayName())){
                    UHCPlugin.eventPlayers.put(p, kit);
                    p.sendMessage("You have selected the " + kit.getName() + " kit");
                    p.closeInventory();
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if(e.getView().getOriginalTitle().equals(ChatColor.RED + "Choose a kit for the next event")) e.setCancelled(true);
    }
}
