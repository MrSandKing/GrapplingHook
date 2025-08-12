package me.mrsandking.grapplinghook.api;

import org.bukkit.inventory.ItemStack;

public interface GrapplingHook {

    /**
     * Returns the id of grappling hook
     * @return id
     */

    String getId();

    /**
     * Returns the display name of grappling hook
     * @return display name
     */

    String getDisplayName();

    /**
     * Returns the multiplier of grappling hook
     * @return multiplier
     */

    double getMultiplier();

    /**
     * Returns the cooldown in seconds of grappling hook
     * @return cooldown
     */

    int getCooldown();

    /**
     * Returns the maximum of multiplied y-coordinates power
     * @return max y-coordinate power
     */

    double getMaxYPower();

}