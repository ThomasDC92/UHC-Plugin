package dev.thomasdc.uhcplugin.models;

import de.tr7zw.nbtapi.NBT;
import dev.thomasdc.uhcplugin.UHCPlugin;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
public class CustomRecipes {
    private final UHCPlugin plugin;
    public static HashMap<ItemStack,ShapedRecipe> recipes = new HashMap<>();
    public static List<String> keys = List.of(
            "srstevespick",
            "srnotchapple"
    );

    public static ItemStack STEVES_PICK = createItem(Material.IRON_PICKAXE, 1, ChatColor.GRAY.toString() + ChatColor.BOLD + "Steve's good ol' pick", new HashMap<Enchantment, Integer>() {{
        put(Enchantment.DIG_SPEED, 2);
        put(Enchantment.DURABILITY, 3);
    }},"steves_pick", List.of(ChatColor.LIGHT_PURPLE.toString() + "Instantly melts ores!","(steve not included)"));



    public static List<ItemStack> items = List.of(
            STEVES_PICK,
            new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 1)
    );

    public void registerRecipes() {
        ShapedRecipe stevePickRecipe = new ShapedRecipe(new NamespacedKey(plugin, "srstevespick"), STEVES_PICK);
        stevePickRecipe.shape(
                "III",
                "CSC",
                "CSC"
        );
        stevePickRecipe.setIngredient('I', Material.RAW_IRON);
        stevePickRecipe.setIngredient('S', Material.STICK);
        stevePickRecipe.setIngredient('C', Material.COAL);
        System.out.println(Arrays.toString(stevePickRecipe.getShape()));
        Bukkit.addRecipe(stevePickRecipe);
        recipes.put(STEVES_PICK,stevePickRecipe);


        ShapedRecipe notchAppleRecipe = new ShapedRecipe(new NamespacedKey(plugin, "srnotchapple"), new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 1));
        notchAppleRecipe.shape(
                "GGG",
                "GAG",
                "GGG"
        );
        notchAppleRecipe.setIngredient('G', Material.GOLD_BLOCK);
        notchAppleRecipe.setIngredient('A', Material.APPLE);
        Bukkit.addRecipe(notchAppleRecipe);
        recipes.put(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 1),notchAppleRecipe);
    }


    public static ItemStack createItem(Material material, int amount, String name, HashMap<Enchantment, Integer> enchantments, String key, List<String> lore) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        for (Enchantment enchantment : enchantments.keySet()) {
            meta.addEnchant(enchantment, enchantments.get(enchantment), true);
        }
        item.setItemMeta(meta);
        NBT.modify(item,nbt->{
            nbt.setString(key,key);
        });
        return item;
    }

    public static ShapedRecipe getRecipeShapeByItem(ItemStack item){
        ShapedRecipe recipe = recipes.get(item);
        if(recipe == null) return null;
        return recipe;
    }
}
