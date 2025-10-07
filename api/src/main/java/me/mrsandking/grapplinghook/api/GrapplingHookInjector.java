package me.mrsandking.grapplinghook.api;

import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public interface GrapplingHookInjector {

    /**
     * Returns the grappling hook object based on given item stack.
     * This function will return null, if it is not grappling hook.
     *
     * @param itemStack - given item stack to get grappling hook object
     * @return grappling hook
     */

    GrapplingHook getGrapplingHook(ItemStack itemStack);

    /**
     * Returns the item stack of created grappling hook.
     * This item will contain display name, lore, will be registered on player
     * based on given unique id. This function calls increasing serial numbers.
     *
     * @param grapplingHook - this is the base for item stack
     * @param uniqueId - owner's unique id
     * @return item stack
     */

    ItemStack createGrapplingHook(GrapplingHook grapplingHook, UUID uniqueId);

}