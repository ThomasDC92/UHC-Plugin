package dev.thomasdc.uhcplugin.commands;

import dev.thomasdc.uhcplugin.UHCPlugin;
import dev.thomasdc.uhcplugin.models.CustomRecipes;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import java.util.Arrays;

public class CustomItems implements CommandExecutor, Listener {
    private final Inventory inv;
    private Inventory recipeInv;

    public CustomItems() {
        inv = Bukkit.createInventory(null, 27, ChatColor.RED + "Check recipes");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (command.getName().equals("customItems")) {
                Player p = (Player) sender;
                inv.clear();
                for (ItemStack item : CustomRecipes.items) {
                    inv.addItem(item);
                }
                p.openInventory(inv);
            }
        }

        return true;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getView().getOriginalTitle().equals(ChatColor.RED + "Check recipes")) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null) return;
            if (e.getCurrentItem().getItemMeta() == null) return;
            if (e.getCurrentItem().getItemMeta().getDisplayName() == null) return;
            Player p = (Player) e.getWhoClicked();
            p.closeInventory();
            recipeInv = Bukkit.createInventory(null, 27, ChatColor.RED + "Recipe");
            ShapedRecipe shapedRecipe = CustomRecipes.getRecipeShapeByItem(e.getCurrentItem());
            String[] shape = shapedRecipe.getShape();
            int[][] slots = {{2,3,4},{11,12,13},{20,21,22}} ;
            for (int i = 0; i < shape.length; i++) {
                for (int j = 0; j < 3; j++) {
                    recipeInv.setItem(slots[i][j],shapedRecipe.getIngredientMap().get(shape[i].charAt(j)));
                }
            }
            recipeInv.setItem(16, e.getCurrentItem());

            p.openInventory(recipeInv);
        }
        if(e.getView().getOriginalTitle().equals(ChatColor.RED + "Recipe")){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryDragEvent e) {
        if (e.getView().getOriginalTitle().equals(ChatColor.RED + "Check recipes"))
            e.setCancelled(true);

        if(e.getView().getOriginalTitle().equals(ChatColor.RED + "Recipe"))
            e.setCancelled(true);
    }
}
