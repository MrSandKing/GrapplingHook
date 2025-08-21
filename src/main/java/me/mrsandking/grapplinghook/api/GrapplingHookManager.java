package me.mrsandking.grapplinghook.api;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.UUID;

public interface GrapplingHookManager {

    List<GrapplingHook> getGrapplingHooks();

    void load(JavaPlugin plugin);

    GrapplingHook getGrapplingHook(ItemStack itemStack);

    GrapplingHook getGrapplingHook(String id);

    void applyCooldown(Player player, GrapplingHook grapplingHook);

    boolean isGrapplingHook(Player player, ItemStack itemStack);

    boolean hasFallDamage(UUID uuid);

    boolean hasCooldown(UUID uuid);

    long getCooldown(UUID uuid);

    ItemStack createGrapplingHook(GrapplingHook grapplingHook, UUID uniqueId);

}