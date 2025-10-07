/**
 * BookItemMenu is essentially paginated version of ItemMenu class.
 * -
 * It allows to create interesting inventories that will be fully functional.
 * All inventories from BookItemMenu have at least 2 rows.
 */

package me.mrsandking.grapplinghook.api.inventory;

import me.mrsandking.grapplinghook.api.event.inventory.ItemClickEvent;
import me.mrsandking.grapplinghook.api.inventory.item.MenuItem;
import me.mrsandking.grapplinghook.api.inventory.item.NextMenuItem;
import me.mrsandking.grapplinghook.api.inventory.item.ReturnMenuItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class BookItemMenu extends ItemMenu {

    private final Map<Integer, BookPage> pages;
    private final List<NumerableMenuItem> realItems;
    private final BookPage mainPage;
    private MenuItem[] optionsItem;


    /**
     * This constructor creates paginated menu.
     * There are few types of paginated menu.
     * @param title - inventory's title
     * @param bookMenuType - paginated menu type
     * @param size - size of menu
     * @param icons - all icons displayed on each page
     * @param optionsItem - additional items in last row
     * @param parent - menu that will be
     */

    public BookItemMenu(String title, BookMenuType bookMenuType, ItemMenu.Size size, List<MenuItem> icons, MenuItem[] optionsItem, ItemMenu parent) {
        super(title, size);
        this.pages = new LinkedHashMap<>();
        this.realItems = new LinkedList<>();
        this.optionsItem = optionsItem;

        // Cannot create one-line paginated menu, so it won't do anything.
        if (size == ItemMenu.Size.ONE_LINE) {
            throw new IllegalStateException("Cannot create paginated menu, because it is only one-rowed!");
        }

        if (bookMenuType == BookMenuType.FRAMED && size == Size.TWO_LINE) {
            throw new IllegalStateException("Cannot create framed paginated menu, because of not enough lines!");
        }

        // Initialize how many items will be displayed on each page
        int page = this.initializeItems(size, bookMenuType, icons);

        // Set main page and its parent
        this.mainPage = new BookPage(title + " | Page 1", size, 0);
        this.mainPage.setParent(parent);

        // Register required pages
        for (int i = 0; i < page; i++) {
            this.pages.put(i, new BookPage(title + " | Page " + (i + 2), size, i));
        }

        AtomicInteger slot = new AtomicInteger(0);
        AtomicInteger next = new AtomicInteger(0);

        pages.values().forEach(bookPage -> {
            slot.set(bookPage.getPageNumber() - 1);
            setupNavigationButtons(slot, next, bookPage, size, bookMenuType);
            bookPage.setParent(parent);
        });

        if (page > 0) {
            NextMenuItem set = new NextMenuItem(this.pages.get(0));
            set.setNextMenu(this.pages.get(0));
            if (bookMenuType == BookMenuType.HYPIXEL || bookMenuType == BookMenuType.CLASSIC) {
                this.mainPage.setItem(size.getSize() - 1, set);
            } else {
                this.mainPage.setItem(26, set);
            }
        }

        this.loadPages(next, bookMenuType);
    }

    public BookItemMenu(String title, BookMenuType bookMenuType, ItemMenu.Size size, List<MenuItem> icons, MenuItem[] optionsItem) {
        this(title, bookMenuType, size, icons, optionsItem, null);
    }

    /**
     * This function allows to set option items. These items are displayed in last row.
     * @param newOptionsItem - array of option items.
     */

    public void setOptionItems(MenuItem[] newOptionsItem) {
        if (newOptionsItem != null && newOptionsItem.length > 0) {
            this.optionsItem = newOptionsItem;

            for(int x = 0; x < this.optionsItem.length; ++x) {
                MenuItem set = this.optionsItem[x];
                int slot = 45 + x;
                if (set != null && slot < 52) {
                    this.mainPage.setItem(slot, set);
                    this.pages.values().forEach(bookPage -> bookPage.setItem(slot, set));
                }
            }

        }
    }

    /**
     * Return all items from specific page.
     * @param page - number of specific page
     */

    private List<NumerableMenuItem> getItemsFromPage(int page) {
        return realItems.stream()
                .filter(numerableMenuItem -> numerableMenuItem.page == page)
                .toList();
    }

    @Override
    public void open(Player player) {
        if (player == null) {
            throw new NullPointerException("Player must be online.");
        }
        this.mainPage.open(player);
    }

    private int getSlotsByMenuType(Size size, BookMenuType bookMenuType) {
        // Initialize how many items will be displayed on each page.
        if (bookMenuType == BookMenuType.CLASSIC) {
            return size.getSize() - 9;
        } else if (bookMenuType == BookMenuType.HYPIXEL) {
            return 21;
        } else if (bookMenuType == BookMenuType.HYPIXEL_LEVELS) {
            return 25;
        } else if (bookMenuType == BookMenuType.FRAMED) {
            return 7 * (size.getRows() - 2);
        } else if (bookMenuType == BookMenuType.TIERED) {
            return 35;
        }

        return 0;
    }

    private void fillPage(BookPage pageFinal, int page, AtomicInteger next, BookMenuType bookMenuType) {
        if (bookMenuType == BookMenuType.CLASSIC) {
            next.set(0);

            this.getItemsFromPage(page).stream()
                    .filter(Objects::nonNull)
                    .forEach(numerableMenuItem -> pageFinal.setItem(next.getAndIncrement(), numerableMenuItem));

        } else if (bookMenuType == BookMenuType.HYPIXEL) {
            next.set(10);
            List<NumerableMenuItem> items = this.getItemsFromPage(page);
            items.forEach(numerableMenuItem -> {
                if (next.get() == 17 || next.get() == 26) {
                    next.set(next.get() + 2);
                }
                pageFinal.setItem(next.getAndIncrement(), numerableMenuItem);
            });
        } else if (bookMenuType == BookMenuType.HYPIXEL_LEVELS) {
            next.set(0);
            List<NumerableMenuItem> items = this.getItemsFromPage(page);
            items.forEach(numerableMenuItem -> {
                if (next.get() == 18 || next.get() == 26) {
                    next.set(next.get() + 1);
                }
                pageFinal.setItem(next.getAndIncrement(), numerableMenuItem);
            });
        } else if (bookMenuType == BookMenuType.FRAMED) {
            next.set(10);
            List<NumerableMenuItem> items = this.getItemsFromPage(page);
            items.forEach(numerableMenuItem -> {
                if (next.get() == 17 || next.get() == 26 || next.get() == 35 || next.get() == 44) {
                    next.set(next.get() + 2);
                }
                pageFinal.setItem(next.getAndIncrement(), numerableMenuItem);
            });

            for (int i = 0; i < getSize().getSize(); i++) {
                if (i < 9 || i >= (getSize().getSize() - 9)|| i % 9 == 0 || i % 9 == 8) {
                    pageFinal.setItem(i, new MenuItem(" ", new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15)));
                }
            }
        } else if (bookMenuType == BookMenuType.TIERED) {
            AtomicInteger base = new AtomicInteger(1);
            next.set(base.get());
            List<NumerableMenuItem> items = this.getItemsFromPage(page);
            AtomicInteger counter = new AtomicInteger(0);
            items.forEach(numerableMenuItem -> {
                if (counter.get() == 5) {
                    counter.set(0);
                    next.set(base.incrementAndGet());
                }
                pageFinal.setItem(next.get(), numerableMenuItem);
                next.set(next.get() + 9);
                counter.set(counter.get() + 1);
            });
        }
    }

    private void setupNavigationButtons(AtomicInteger slot, AtomicInteger next, BookPage bookPage, Size size, BookMenuType bookMenuType) {
        if (bookMenuType == BookMenuType.CLASSIC || bookMenuType == BookMenuType.HYPIXEL || bookMenuType == BookMenuType.FRAMED || bookMenuType == BookMenuType.TIERED) {
            if (slot.get() >= 0) {
                ItemMenu returnMenu = this.pages.get(slot.get());
                if (returnMenu != null) {
                    ReturnMenuItem returnMenuItem = new ReturnMenuItem(returnMenu);
                    returnMenuItem.setReturnMenu(returnMenu);
                    bookPage.setItem(size.getSize() - 2, returnMenuItem);
                }
            } else {
                ReturnMenuItem returnMenuItem = new ReturnMenuItem(this.mainPage);
                returnMenuItem.setReturnMenu(this.mainPage);
                bookPage.setItem(size.getSize() - 2, returnMenuItem);
            }

            next.set(bookPage.getPageNumber() + 1);
            ItemMenu nextMenu = this.pages.get(next.get());
            if (nextMenu != null) {
                NextMenuItem set = new NextMenuItem(nextMenu);
                set.setNextMenu(nextMenu);
                bookPage.setItem(size.getSize() - 1, set);
            }

        } else {
            if (slot.get() >= 0) {
                ItemMenu returnMenu = this.pages.get(slot.get());
                if (returnMenu != null) {
                    ReturnMenuItem returnMenuItem = new ReturnMenuItem(returnMenu);
                    returnMenuItem.setReturnMenu(returnMenu);
                    bookPage.setItem(18, returnMenuItem);
                }
            } else {
                ReturnMenuItem returnMenuItem = new ReturnMenuItem(this.mainPage);
                returnMenuItem.setReturnMenu(this.mainPage);
                bookPage.setItem(18, returnMenuItem);
            }

            next.set(bookPage.getPageNumber() + 1);
            ItemMenu nextMenu = this.pages.get(next.get());
            if (nextMenu != null) {

                NextMenuItem set = new NextMenuItem(nextMenu);
                set.setNextMenu(nextMenu);
                bookPage.setItem(26, set);
            }
        }
    }

    private void loadPages(AtomicInteger next, BookMenuType bookMenuType) {
        List<BookPage> realPagesNumber = new LinkedList<>();
        realPagesNumber.add(this.mainPage);
        realPagesNumber.addAll(pages.values());

        for (int x = 0; x < realPagesNumber.size(); ++x) {
            BookPage pageFinal = realPagesNumber.get(x);
            if (pageFinal != null) {
                fillPage(pageFinal, x, next, bookMenuType);
            }
        }
    }

    private int initializeItems(Size size, BookMenuType bookMenuType, List<MenuItem> icons) {
        int sizeForIconsPerPage = this.getSlotsByMenuType(size, bookMenuType);
        int page = 0;
        int counter = 0;

        for (MenuItem menuItem : icons) {
            if (counter == sizeForIconsPerPage) {
                counter = 0;
                page++;
            }

            this.realItems.add(new NumerableMenuItem(menuItem, page));
            counter++;
        }

        return page;
    }

    public static final class BookPage extends ItemMenu {
        private final int number;

        public BookPage(String name, Size size, int pageNumber) {
            super(name, size);
            this.number = pageNumber;
        }

        public int getPageNumber() {
            return this.number;
        }
    }

    private static class NumerableMenuItem extends MenuItem {
        private final int page;
        private final MenuItem original;

        public NumerableMenuItem(MenuItem item, int page) {
            super(item.getDisplayName(), item.getIcon(), item.getLore().toArray(String[]::new));
            this.page = page;
            this.original = item;
        }

        @Override
        public void onItemClick(ItemClickEvent event) {
            if (this.original != null) {
                this.original.onItemClick(event);
            }

        }
    }

    public enum BookMenuType {
        CLASSIC,

        HYPIXEL,

        HYPIXEL_LEVELS,

        FRAMED,

        TIERED;
    }
}