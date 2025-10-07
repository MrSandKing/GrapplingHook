package me.mrsandking.grapplinghook.manager;

import lombok.Getter;
import me.mrsandking.grapplinghook.GrapplingHookMain;
import me.mrsandking.grapplinghook.api.GrapplingHook;
import me.mrsandking.grapplinghook.api.GrapplingHookManager;
import me.mrsandking.grapplinghook.api.util.ColourUtil;
import me.mrsandking.grapplinghook.object.GrapplingHookImpl;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class GrapplingHookManagerImpl implements GrapplingHookManager {

    private @Getter List<GrapplingHook> grapplingHooks;

    private static final String COOLDOWN_METADATA = "COOLDOWN";
    private static final String FALL_DAMAGE_METADATA = "FALL_DAMAGE";

    public static String COOLDOWN_MESSAGE;
    public static List<String> STATS_LORE;

    public GrapplingHookManagerImpl(JavaPlugin plugin) {
        this.load(plugin);
    }

    @Override
    public void load(JavaPlugin plugin) {
        this.grapplingHooks = new ArrayList<>();

        if (!plugin.getDataFolder().exists()) {
            plugin.saveDefaultConfig();
            plugin.saveConfig();
        }

        plugin.reloadConfig();

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
    public int increaseSerialNumber() {
        return ++GrapplingHookImpl.SERIAL_NUMBER;
    }

    @Override
    public GrapplingHook getGrapplingHook(String id) {
        return grapplingHooks.stream()
                .filter(hook -> hook.getId().equalsIgnoreCase(id))
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
        return getCooldown(uuid) > 0;
    }

    @Override
    public long getCooldown(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null || !player.isOnline()) {
            return 0;
        }

        if (player.getMetadata(COOLDOWN_METADATA).isEmpty()) {
            return 0;
        }

        return (player.getMetadata(COOLDOWN_METADATA).get(0).asLong() - System.currentTimeMillis()) / 1000;
    }

    @Override
    public void save() {
        GrapplingHookMain.getInstance().getConfig().set("SerialNumber", GrapplingHookImpl.SERIAL_NUMBER);
        GrapplingHookMain.getInstance().saveConfig();
    }

    @Override
    public List<String> getStatsLore() {
        return STATS_LORE;
    }

}