package me.mrsandking.grapplinghook;

import lombok.Getter;
import me.mrsandking.grapplinghook.api.GrapplingHookManager;
import me.mrsandking.grapplinghook.api.command.BaseCommand;
import me.mrsandking.grapplinghook.api.inventory.handler.ItemMenuListener;
import me.mrsandking.grapplinghook.command.CommandHandler;
import me.mrsandking.grapplinghook.listener.GrapplingListener;
import me.mrsandking.grapplinghook.manager.GrapplingHookManagerImpl;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

public final class GrapplingHookMain extends JavaPlugin {

    private static @Getter GrapplingHookMain instance;
    private @Getter GrapplingHookManager grapplingHookManager;

    @Override
    public void onEnable() {
        instance = this;

        this.grapplingHookManager = new GrapplingHookManagerImpl(this);

        registerCommand("grapplinghook", CommandHandler.class);

        getServer().getPluginManager().registerEvents(new ItemMenuListener(), this);
        getServer().getPluginManager().registerEvents(new GrapplingListener(), this);
    }

    @Override
    public void onDisable() {
        saveConfig();
    }

    public void registerCommand(String command, Class<? extends BaseCommand> commandClass) {
        try {
            Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

            commandMap.register(command, commandClass.getConstructor().newInstance());
        } catch(Exception e) {
           // Util.sendConsoleMessage(MessageType.ERROR, "Could not properly register command " + e.getMessage());
        }
    }
}