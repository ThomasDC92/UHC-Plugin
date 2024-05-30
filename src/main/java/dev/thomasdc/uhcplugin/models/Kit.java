package dev.thomasdc.uhcplugin.models;

import lombok.Builder;
import lombok.Data;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Data
@Builder
public class Kit {
    private String name;
    private List<ItemStack> items;
    private ItemStack icon;
}
