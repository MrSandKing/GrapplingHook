package me.mrsandking.grapplinghook.api.inventory.item;

import me.mrsandking.grapplinghook.GrapplingHookMain;
import me.mrsandking.grapplinghook.api.event.inventory.ItemClickEvent;
import me.mrsandking.grapplinghook.api.inventory.ItemMenu;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

public class OpenOtherMenuItem extends MenuItem {

    private final ItemMenu newMenu;

    public OpenOtherMenuItem(String displayName, ItemStack icon, ItemMenu newMenu, String... lore) {
        super(displayName, icon, lore);
        this.newMenu = newMenu;
    }

    @Override
    public void onItemClick(ItemClickEvent event) {
        Bukkit.getScheduler().runTaskLater(GrapplingHookMain.getInstance(), () -> newMenu.open(event.getPlayer()), 2L);
    }
}