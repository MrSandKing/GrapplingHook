package me.mrsandking.grapplinghook.api.inventory.item;

import lombok.Setter;
import me.mrsandking.grapplinghook.api.event.inventory.ItemClickEvent;
import me.mrsandking.grapplinghook.api.inventory.ItemMenu;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Setter
public class ReturnMenuItem extends MenuItem {

    private ItemMenu returnMenu;

    public ReturnMenuItem(ItemMenu returnMenu) {
        super("", new ItemStack(Material.ARROW));
        this.returnMenu = returnMenu;
    }

    @Override
    public void onItemClick(ItemClickEvent event) {
        if (this.returnMenu != null) {
            this.returnMenu.open(event.getPlayer());
        } else {
            event.setWillClose(true);
        }
    }
}