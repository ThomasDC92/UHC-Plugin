package dev.thomasdc.uhcplugin.models;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class KitItems {
    public ItemStack KETAMINE = new ItemStack(Material.POTION);
    public ItemStack FISHBOYS_ROD = new ItemStack(Material.FISHING_ROD);

    public KitItems(){
        initializeKetamine();
        initializeFishboysRod();
    }

    public void initializeKetamine(){
        PotionMeta ketamineMeta = (PotionMeta) KETAMINE.getItemMeta();
        ketamineMeta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 20*30, 1), true);
        ketamineMeta.addCustomEffect(new PotionEffect(PotionEffectType.REGENERATION, 20*30, 2), true);
        ketamineMeta.addCustomEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*30, 1), true);
        ketamineMeta.addCustomEffect(new PotionEffect(PotionEffectType.POISON, 20*35, 1), true);
        ketamineMeta.setDisplayName(ChatColor.RED + "Ketamine");
        ketamineMeta.addEnchant(Enchantment.BINDING_CURSE, 1, true);
        KETAMINE.setItemMeta(ketamineMeta);
    }

    public void initializeFishboysRod(){
        ItemMeta fishboysRodMeta = FISHBOYS_ROD.getItemMeta();
        fishboysRodMeta.setDisplayName(ChatColor.GREEN + "Fishboy's Rod");
        fishboysRodMeta.addEnchant(Enchantment.LUCK, (int)(Math.random()*5)+15, true);
        fishboysRodMeta.addEnchant(Enchantment.LURE, (int)(Math.random()*5)+15, true);
        fishboysRodMeta.setUnbreakable(true);
        FISHBOYS_ROD.setItemMeta(fishboysRodMeta);
    }

    public void initialize(){

    }

}
