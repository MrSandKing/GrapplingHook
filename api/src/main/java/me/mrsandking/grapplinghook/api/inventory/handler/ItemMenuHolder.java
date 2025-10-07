package me.mrsandking.grapplinghook.api.inventory.handler;

import lombok.Getter;
import me.mrsandking.grapplinghook.api.inventory.ItemMenu;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

@Getter
public class ItemMenuHolder implements InventoryHolder {

    private final ItemMenu menu;
    private final Inventory inventory;

    public ItemMenuHolder(ItemMenu menu, Inventory inventory) {
        this.menu = menu;
        this.inventory = inventory;
    }

}