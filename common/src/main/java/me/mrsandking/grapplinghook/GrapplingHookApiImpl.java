package me.mrsandking.grapplinghook;

import me.mrsandking.grapplinghook.api.GrapplingHookApi;
import me.mrsandking.grapplinghook.api.GrapplingHookInjector;
import me.mrsandking.grapplinghook.api.GrapplingHookManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class GrapplingHookApiImpl extends GrapplingHookApi {

    GrapplingHookApiImpl(JavaPlugin pluginHook) {
        super(pluginHook);
    }

    @Override
    public GrapplingHookManager getGrapplingHookManager() {
        return GrapplingHookMain.getInstance().getGrapplingHookManager();
    }

    @Override
    public GrapplingHookInjector getGrapplingHookInjector() {
        return GrapplingHookMain.getInstance().getGrapplingHookInjector();
    }
}