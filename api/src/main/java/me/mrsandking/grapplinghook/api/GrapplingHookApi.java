package me.mrsandking.grapplinghook.api;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class GrapplingHookApi {

    private static @Getter GrapplingHookApi instance;
    private static @Getter JavaPlugin plugin;

    protected GrapplingHookApi(JavaPlugin pluginHook) {
        instance = this;
        plugin = pluginHook;
    }

    public abstract GrapplingHookManager getGrapplingHookManager();

    public abstract GrapplingHookInjector getGrapplingHookInjector();

}