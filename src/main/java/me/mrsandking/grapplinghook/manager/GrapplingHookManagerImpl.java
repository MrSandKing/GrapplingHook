package me.mrsandking.grapplinghook.manager;

import lombok.Getter;
import me.mrsandking.grapplinghook.GrapplingHookMain;
import me.mrsandking.grapplinghook.api.GrapplingHook;
import me.mrsandking.grapplinghook.api.GrapplingHookManager;
import me.mrsandking.grapplinghook.api.util.ColourUtil;
import me.mrsandking.grapplinghook.object.GrapplingHookImpl;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class GrapplingHookManagerImpl implements GrapplingHookManager {

    private final @Getter List<GrapplingHook> grapplingHooks;

    private static final String COOLDOWN_METADATA = "COOLDOWN";
    private static final String FALL_DAMAGE_METADATA = "FALL_DAMAGE";

    public static String COOLDOWN_MESSAGE;
    public static List<String> STATS_LORE;

    public GrapplingHookManagerImpl(JavaPlugin plugin) {
        this.grapplingHooks = new ArrayList<>();

        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        plugin.saveConfig();

        FileConfiguration config = plugin.getConfig();
        ConfigurationSection section = config.getConfigurationSection("GrapplingHooks");

        COOLDOWN_MESSAGE = ColourUtil.colorize(config.getString("Cooldown-Message"));
        STATS_LORE = ColourUtil.colouredLore(config.getStringList("Stats-Lore"));

        if (section == null) {
            return;
        }

        for (String id : section.getKeys(false)) {
            GrapplingHook grapplingHook = new GrapplingHookImpl(
                    id,
                    section.getString(id + ".DisplayName", "&6&lGrappling Hook"),
                    section.getDouble(id + ".Multiplier", 2.0),
                    section.getInt(id + ".Cooldown", 10),
                    section.getDouble(id + ".MaxYPower", 1.1)
            );

            grapplingHooks.add(grapplingHook);
        }
    }

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

        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);

        return grapplingHooks.stream()
                .filter(hook -> nmsStack.hasTag())
                .filter(hook ->
                        nmsStack.getTag().getString("GrapplingHookTag").equals(hook.getId())
                        && nmsStack.getTag().getInt("GrapplingHookSerialNumber") > 0)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void applyCooldown(Player player, GrapplingHook grapplingHook) {
        long cooldown = System.currentTimeMillis() + (grapplingHook.getCooldown() * 1000L);
        long fallDamage = System.currentTimeMillis() + (2 * 1000);

        player.setMetadata(COOLDOWN_METADATA, new FixedMetadataValue(GrapplingHookMain.getInstance(), cooldown));
        player.setMetadata(FALL_DAMAGE_METADATA, new FixedMetadataValue(GrapplingHookMain.getInstance(), fallDamage));
    }

    @Override
    public boolean isGrapplingHook(Player player, ItemStack itemStack) {
        return player != null && getGrapplingHook(itemStack) != null;
    }

    @Override
    public boolean hasFallDamage(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null || !player.isOnline()) {
            return false;
        }

        if (player.getMetadata(FALL_DAMAGE_METADATA).isEmpty()) {
            return false;
        }

        long fallDamage = player.getMetadata(FALL_DAMAGE_METADATA).get(0).asLong();
        return fallDamage > System.currentTimeMillis();
    }

    @Override
    public boolean hasCooldown(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null || !player.isOnline()) {
            return false;
        }

        if (player.getMetadata(COOLDOWN_METADATA).isEmpty()) {
            return false;
        }

        long fallDamage = player.getMetadata(COOLDOWN_METADATA).get(0).asLong();
        return fallDamage > System.currentTimeMillis();
    }

    @Override
    public ItemStack createGrapplingHook(GrapplingHook grapplingHook, UUID uniqueId) {
        ItemStack itemStack = new ItemStack(Material.FISHING_ROD, 1);

        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound tagCompound = nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound();
        tagCompound.setString("GrapplingHookTag", grapplingHook.getId());
        tagCompound.setString("GrapplingHookOwner", Bukkit.getPlayer(uniqueId).getDisplayName());
        tagCompound.setInt("GrapplingHookSerialNumber", ++GrapplingHookImpl.SERIAL_NUMBER);

        nmsStack.setTag(tagCompound);

        itemStack = CraftItemStack.asBukkitCopy(nmsStack);

        if (itemStack.getItemMeta() != null) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(ColourUtil.colorize(grapplingHook.getDisplayName()));

            final List<String> finalLore = new ArrayList<>();
            for (String string : GrapplingHookManagerImpl.STATS_LORE) {
                finalLore.add(string
                        .replace("%MULTIPLIER%", String.valueOf(grapplingHook.getMultiplier()))
                        .replace("%COOLDOWN%", String.valueOf(grapplingHook.getCooldown()))
                        .replace("%SERIAL_NUMBER%", String.valueOf(GrapplingHookImpl.SERIAL_NUMBER))
                        .replace("%OWNER%", String.valueOf(nmsStack.getTag().getString("GrapplingHookOwner"))));
            }

            itemMeta.setLore(finalLore);
            itemStack.setItemMeta(itemMeta);
        }

        GrapplingHookMain.getInstance().getConfig().set("SerialNumber", GrapplingHookImpl.SERIAL_NUMBER);
        GrapplingHookMain.getInstance().saveConfig();

        return itemStack;
    }
}