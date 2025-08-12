package me.mrsandking.grapplinghook.api;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public interface GrapplingHookManager {

    List<GrapplingHook> getGrapplingHooks();

    GrapplingHook getGrapplingHook(ItemStack itemStack);

    void applyCooldown(Player player, GrapplingHook grapplingHook);

    boolean isGrapplingHook(Player player, ItemStack itemStack);

    boolean hasFallDamage(UUID uuid);

    boolean hasCooldown(UUID uuid);

    ItemStack createGrapplingHook(GrapplingHook grapplingHook, UUID uniqueId);

}