package me.mrsandking.grapplinghook.api.inventory.item;

import me.mrsandking.grapplinghook.api.event.inventory.ItemClickEvent;
import org.bukkit.inventory.ItemStack;

public class BackMenuItem extends MenuItem {

    public BackMenuItem(String displayName, ItemStack icon, String... lore) {
        super(displayName, icon, lore);
    }

    @Override
    public void onItemClick(ItemClickEvent event) {
        event.setWillGoBack(true);
    }

}