package me.mrsandking.grapplinghook.api.inventory.item;

import me.mrsandking.grapplinghook.api.event.inventory.ItemClickEvent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CloseMenuItem extends MenuItem {

	public CloseMenuItem() {
		super("", new ItemStack(Material.BARRIER));
	}

	@Override
	public void onItemClick(ItemClickEvent event) {
		event.setWillClose(true);
	}
}