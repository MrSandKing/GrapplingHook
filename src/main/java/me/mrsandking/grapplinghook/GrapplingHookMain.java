package me.mrsandking.grapplinghook;

import lombok.Getter;
import me.mrsandking.grapplinghook.api.GrapplingHookManager;
import me.mrsandking.grapplinghook.api.inventory.handler.ItemMenuListener;
import me.mrsandking.grapplinghook.command.GrapplingHookCommand;
import me.mrsandking.grapplinghook.listener.GrapplingListener;
import me.mrsandking.grapplinghook.manager.GrapplingHookManagerImpl;
import org.bukkit.plugin.java.JavaPlugin;

public final class GrapplingHookMain extends JavaPlugin {

    private static @Getter GrapplingHookMain instance;
    private @Getter GrapplingHookManager grapplingHookManager;

    @Override
    public void onEnable() {
        instance = this;

        this.grapplingHookManager = new GrapplingHookManagerImpl(this);

        getCommand("grapplinghook").setExecutor(new GrapplingHookCommand());

        getServer().getPluginManager().registerEvents(new ItemMenuListener(), this);
        getServer().getPluginManager().registerEvents(new GrapplingListener(), this);
    }

    @Override
    public void onDisable() {
        saveConfig();
    }
}