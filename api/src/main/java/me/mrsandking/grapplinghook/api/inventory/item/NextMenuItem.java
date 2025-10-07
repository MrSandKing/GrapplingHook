package me.mrsandking.grapplinghook.api.inventory.item;

import lombok.Setter;
import me.mrsandking.grapplinghook.api.event.inventory.ItemClickEvent;
import me.mrsandking.grapplinghook.api.inventory.ItemMenu;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Setter
public class NextMenuItem extends MenuItem {

    private ItemMenu nextMenu;

    public NextMenuItem(ItemMenu nextMenu) {
        super("", new ItemStack(Material.ARROW));
        this.nextMenu = nextMenu;
    }

    @Override
    public void onItemClick(ItemClickEvent event) {
        if (this.nextMenu != null) {
            this.nextMenu.open(event.getPlayer());
            event.setWillUpdate(true);
        } else {
            event.setWillClose(true);
        }

    }
}