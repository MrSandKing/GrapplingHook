package me.mrsandking.grapplinghook.api;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.UUID;

public interface GrapplingHookManager {

    /**
     * Returns the list of loaded grappling hooks
     * @return list
     */

    List<GrapplingHook> getGrapplingHooks();

    /**
     * Loads variables, configuration, grappling hooks, etc.
     * This may be called using Skript, other plugin/addon.
     * @param plugin - we need GrapplingHookMain instance to call properly.
     */

    void load(JavaPlugin plugin);

    /**
     * Increase and return new value of serial numbers
     * @return int
     */

    int increaseSerialNumber();

    /**
     * Returns the grappling hook object based on given id.
     * @param id - given id
     * @return grappling hook
     */

    GrapplingHook getGrapplingHook(String id);

    /**
     * Applies cooldown to the player on specific grappling hook
     * @param player - player to give cooldown
     * @param grapplingHook - this is where is hiding our cooldown
     */

    void applyCooldown(Player player, GrapplingHook grapplingHook);

    /**
     * This allows us to check if player has fall damage or not.
     *
     * @param uuid - we need to know which player
     * @return result true/false
     */

    boolean hasFallDamage(UUID uuid);

    boolean hasCooldown(UUID uuid);

    long getCooldown(UUID uuid);

    void save();

    List<String> getStatsLore();

}