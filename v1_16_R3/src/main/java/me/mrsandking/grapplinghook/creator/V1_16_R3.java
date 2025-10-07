package me.mrsandking.grapplinghook.creator;

import me.mrsandking.grapplinghook.api.GrapplingHook;
import me.mrsandking.grapplinghook.api.GrapplingHookApi;
import me.mrsandking.grapplinghook.api.GrapplingHookInjector;
import me.mrsandking.grapplinghook.api.util.ColourUtil;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class V1_16_R3 implements GrapplingHookInjector {

    @Override
    public GrapplingHook getGrapplingHook(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return null;
        }

        if (itemStack.getType() != Material.FISHING_ROD) {
            return null;
        }

        if (!itemStack.hasItemMeta()) {
            return null;
        }

        net.minecraft.server.v1_16_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);

        return GrapplingHookApi.getInstance()
                .getGrapplingHookManager()
                .getGrapplingHooks()
                .stream()
                .filter(hook -> nmsStack.hasTag())
                .filter(hook -> nmsStack.getTag() != null)
                .filter(hook ->
                        nmsStack.getTag().getString("GrapplingHookTag").equals(hook.getId())
                                && nmsStack.getTag().getInt("GrapplingHookSerialNumber") > 0)
                .findFirst()
                .orElse(null);
    }

    @Override
    public ItemStack createGrapplingHook(GrapplingHook grapplingHook, UUID uniqueId) {
        ItemStack itemStack = new ItemStack(Material.FISHING_ROD, 1);

        int serialNumber = GrapplingHookApi.getInstance().getGrapplingHookManager().increaseSerialNumber();

        net.minecraft.server.v1_16_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound tagCompound = nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound();

        if (tagCompound == null) {
            tagCompound = new NBTTagCompound();
        }

        tagCompound.setString("GrapplingHookTag", grapplingHook.getId());
        tagCompound.setString("GrapplingHookOwner", Bukkit.getPlayer(uniqueId).getDisplayName());
        tagCompound.setInt("GrapplingHookSerialNumber", serialNumber);

        nmsStack.setTag(tagCompound);

        itemStack = CraftItemStack.asBukkitCopy(nmsStack);

        if (itemStack.getItemMeta() != null) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(ColourUtil.colorize(grapplingHook.getDisplayName()));

            final List<String> finalLore = new ArrayList<>();
            for (String string : GrapplingHookApi.getInstance().getGrapplingHookManager().getStatsLore()) {
                finalLore.add(string
                        .replace("%MULTIPLIER%", String.valueOf(grapplingHook.getMultiplier()))
                        .replace("%COOLDOWN%", String.valueOf(grapplingHook.getCooldown()))
                        .replace("%SERIAL_NUMBER%", String.valueOf(serialNumber))
                        .replace("%OWNER%", String.valueOf(nmsStack.getTag().getString("GrapplingHookOwner")))
                        .replace("%DATE%", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))));
            }

            itemMeta.setLore(finalLore);
            itemStack.setItemMeta(itemMeta);
        }

        GrapplingHookApi.getInstance().getGrapplingHookManager().save();

        return itemStack;
    }

}