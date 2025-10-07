package me.mrsandking.grapplinghook.api.inventory;

import lombok.Getter;
import lombok.Setter;
import me.mrsandking.grapplinghook.api.GrapplingHookApi;
import me.mrsandking.grapplinghook.api.event.inventory.ItemClickEvent;
import me.mrsandking.grapplinghook.api.inventory.handler.ItemMenuHolder;
import me.mrsandking.grapplinghook.api.inventory.handler.ItemMenuListener;
import me.mrsandking.grapplinghook.api.inventory.item.MenuItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.Optional;

/**
 * A Menu controlled by ItemStacks in an Inventory.
 */
@Getter
public class ItemMenu {

    private @Setter String name;
    private Size size;
    private MenuItem[] items;
    private @Setter ItemMenu parent;

    private Inventory inventory;

    public ItemMenu(String name, Size size, ItemMenu parent) {
        this.name = name;
        this.size = size;
        this.items = new MenuItem[size.getSize()];
        this.parent = parent;
    }

    public ItemMenu(String name, Size size) {
        this(name, size, null);
    }

    /**
     * Checks if the {@link MenuItem} has a parent.
     *
     * @return True if the {@link MenuItem} has a
     *         parent, else false.
     */
    public boolean hasParent()
    {
        return parent != null;
    }

    public ItemMenu setItem(int position, MenuItem menuItem) {
        items[position] = menuItem;
        return this;
    }

    public ItemMenu clearItem(int position) {
        items[position] = null;
        return this;
    }

    public ItemMenu clearAllItems() {
        Arrays.fill(items, null);
        return this;
    }

    /**
     * Fills all empty slots in the {@link MenuItem}
     * with a certain {@link MenuItem}.
     *
     * @param menuItem
     *            The {@link MenuItem}.
     * @return The {@link MenuItem}.
     */
    public ItemMenu fillEmptySlots(MenuItem menuItem) {
        for (int i = 0; i < items.length; i++) {
            if (items[i] == null) {
                items[i] = menuItem;
            }
        }
        return this;
    }

    public void open(Player player) {
        if (!ItemMenuListener.getInstance().isRegistered(GrapplingHookApi.getPlugin())) {
            ItemMenuListener.getInstance().register(GrapplingHookApi.getPlugin());
        }
        inventory = Bukkit.createInventory(new ItemMenuHolder(this,
                        Bukkit.createInventory(player, size.getSize())),
                size.getSize(), name);
        apply(inventory);
        player.openInventory(inventory);
    }

    public void update(Player player) {
        if (player.getOpenInventory() != null) {
            inventory = player.getOpenInventory().getTopInventory();
            if (inventory.getHolder() instanceof ItemMenuHolder
                    && ((ItemMenuHolder) inventory.getHolder()).getMenu()
                    .equals(this)) {
                apply(inventory);
                player.updateInventory();
            }
        }
    }

    public void fillOutLine(MenuItem menuItem) {
        if (this.size == Size.ONE_LINE || size == Size.TWO_LINE) {
            return;
        }

        for (int i = 0; i < getSize().getSize(); i++) {
            if (i < 9 || i >= (getSize().getSize() - 9)|| i % 9 == 0 || i % 9 == 8) {
                setItem(i, menuItem);
            }
        }
    }

    private void apply(Inventory inventory) {
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) {
                inventory.setItem(i, items[i].getFinalIcon());
            }
        }
    }

    public MenuItem getItem(int slot) {
        return slot >= 0 && slot <= size.size ? items[slot] : null;
    }

    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClick() == ClickType.LEFT || event.getClick() == ClickType.RIGHT || event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT) {
            int slot = event.getRawSlot();
            if (slot >= 0 && slot < size.getSize() && items[slot] != null) {
                Player player = (Player) event.getWhoClicked();
                ItemClickEvent itemClickEvent = new ItemClickEvent(player, event.getCurrentItem(), event.getClick());
                items[slot].onItemClick(itemClickEvent);
                if (itemClickEvent.willUpdate()) {
                    update(player);
                } else {
                    player.updateInventory();
                    if (itemClickEvent.willClose()) {
                        final String playerName = player.getName();
                        Bukkit.getScheduler().scheduleSyncDelayedTask(GrapplingHookApi.getPlugin(),
                                () -> Optional.ofNullable(Bukkit.getPlayerExact(playerName)).ifPresent(HumanEntity::closeInventory), 1);
                    }
                    if (itemClickEvent.willGoBack() && hasParent()) {
                        final String playerName = player.getName();
                        Bukkit.getScheduler().scheduleSyncDelayedTask(GrapplingHookApi.getPlugin(),
                                () -> Optional.ofNullable(Bukkit.getPlayerExact(playerName)).ifPresent(optionalPlayer -> {
                                    getParent().open(player);
                                }), 3);
                    }
                }
            }
        }
    }

    /**
     * Destroys the {@link MenuItem}.
     */
    public void destroy()
    {
        name = null;
        size = null;
        items = null;
        parent = null;
    }

    /**
     * Possible sizes of an {@link MenuItem}.
     */
    public enum Size {
        ONE_LINE(9), TWO_LINE(18), THREE_LINE(27), FOUR_LINE(36), FIVE_LINE(45), SIX_LINE(54);

        private final @Getter int size;

        private Size(int size)
        {
            this.size = size;
        }

        public static Size fit(int slots) {
            if (slots < 10) {
                return ONE_LINE;
            } else if (slots < 19) {
                return TWO_LINE;
            } else if (slots < 28) {
                return THREE_LINE;
            } else if (slots < 37) {
                return FOUR_LINE;
            } else if (slots < 46) {
                return FIVE_LINE;
            } else {
                return SIX_LINE;
            }
        }

        public static Size fitFramed(int slots) {
            if (slots < 8) {
                return THREE_LINE;
            } else if (slots < 16) {
                return FOUR_LINE;
            } else if (slots < 24) {
                return FIVE_LINE;
            } else {
                return SIX_LINE;
            }
        }

        public int getRows() {
            return getSize() / 9;
        }

    }
}