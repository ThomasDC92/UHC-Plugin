package dev.thomasdc.uhcplugin.events;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadableItemNBT;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Function;

public class OnBlockBreak implements Listener {
    public List<Material> ores = List.of(
            Material.GOLD_ORE,
            Material.IRON_ORE,
            Material.COPPER_ORE,
            Material.DEEPSLATE_GOLD_ORE,
            Material.DEEPSLATE_IRON_ORE,
            Material.DEEPSLATE_COPPER_ORE
    );
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if(!ores.contains(e.getBlock().getType())) return;
        Player p = e.getPlayer();
        String t = NBT.get(p.getInventory().getItemInMainHand(), (Function<ReadableItemNBT, String>) nbt -> nbt.getString("steves_pick"));
        if(t.equals("steves_pick")){
            World w = e.getBlock().getWorld();
            Location l = e.getBlock().getLocation();
            int amount = e.getBlock().getDrops().stream().toList().get(0).getAmount();
            switch (e.getBlock().getType()) {
                case GOLD_ORE, DEEPSLATE_GOLD_ORE -> {
                    e.setDropItems(false);
                    w.dropItemNaturally(l, new ItemStack(Material.GOLD_INGOT,amount));
                }
                case IRON_ORE, DEEPSLATE_IRON_ORE -> {
                    e.setDropItems(false);
                    w.dropItemNaturally(l, new ItemStack(Material.IRON_INGOT,amount));
                }
                case COPPER_ORE, DEEPSLATE_COPPER_ORE -> {
                    e.setDropItems(false);
                    w.dropItemNaturally(l, new ItemStack(Material.COPPER_INGOT,amount));
                }
            }
        }
    }
}
