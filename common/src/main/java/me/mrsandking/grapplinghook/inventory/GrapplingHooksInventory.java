package me.mrsandking.grapplinghook.inventory;

import me.mrsandking.grapplinghook.GrapplingHookMain;
import me.mrsandking.grapplinghook.api.util.ColourUtil;
import me.mrsandking.grapplinghook.api.GrapplingHook;
import me.mrsandking.grapplinghook.api.event.inventory.ItemClickEvent;
import me.mrsandking.grapplinghook.api.inventory.BookItemMenu;
import me.mrsandking.grapplinghook.api.inventory.item.MenuItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public final class GrapplingHooksInventory extends BookItemMenu {

    public GrapplingHooksInventory() {
        super(
                "Grappling Hooks",
                BookMenuType.FRAMED,
                Size.FOUR_LINE,
                buildItems(),
                null,
                null
        );
    }

    private static List<MenuItem> buildItems() {
        return GrapplingHookMain.getInstance()
                .getGrapplingHookManager()
                .getGrapplingHooks()
                .stream()
                .map(GrapplingHookMenuItem::new)
                .collect(Collectors.toUnmodifiableList());
    }

    private static class GrapplingHookMenuItem extends MenuItem {

        private final GrapplingHook grapplingHook;

        public GrapplingHookMenuItem(GrapplingHook grapplingHook) {
            super(
                    ColourUtil.colorize(grapplingHook.getDisplayName()),
                    new ItemStack(Material.FISHING_ROD),
                    ColourUtil.colouredLore(
                            "&7Multiplier: &6" + grapplingHook.getMultiplier(),
                            "&7Max Y Power: &6" + grapplingHook.getMaxYPower(),
                            "&7Cooldown: &6" + grapplingHook.getCooldown()
                    ).toArray(String[]::new));
            this.grapplingHook = grapplingHook;
        }

        @Override
        public void onItemClick(ItemClickEvent event) {
            event.setWillClose(true);

            if (event.getPlayer().getInventory().firstEmpty() != -1) {
                final ItemStack itemStack = GrapplingHookMain.getInstance().getGrapplingHookInjector().createGrapplingHook(grapplingHook, event.getPlayer().getUniqueId());
                event.getPlayer().getInventory().addItem(itemStack);
                event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.NOTE_PLING, 1.0F, 1.0F);
            }
        }
    }
}