package me.mrsandking.grapplinghook.api.inventory.item;

import lombok.Getter;
import lombok.Setter;
import me.mrsandking.grapplinghook.api.util.ColourUtil;
import me.mrsandking.grapplinghook.api.event.inventory.ItemClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class MenuItem {

    private String displayName;
    private ItemStack icon;
    private List<String> lore;

    public MenuItem(String displayName, ItemStack icon, String... lore) {
        this.displayName = displayName;

        this.icon = icon;
        this.lore = new ArrayList<>();
        this.lore.addAll(ColourUtil.colouredLore(lore));
    }

    public MenuItem addToLoreOnCondition(boolean condition, String... lines) {
        if (condition) {
            lore.addAll(ColourUtil.colouredLore(lines));
        }
        return this;
    }

    /**
     * Get the final menu item icon.
     * @return icon
     */

    public ItemStack getFinalIcon() {
        return setNameAndLore(getIcon().clone(), getDisplayName(), getLore());
    }

    public void onItemClick(ItemClickEvent event) {
        // Do nothing by default
    }

    public static ItemStack setNameAndLore(ItemStack itemStack, String displayName, List<String> lore) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(displayName);
            meta.setLore(lore);
            itemStack.setItemMeta(meta);
        }
        return itemStack;
    }

}