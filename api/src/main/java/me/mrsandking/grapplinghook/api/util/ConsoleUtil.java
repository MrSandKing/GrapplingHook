package me.mrsandking.grapplinghook.api.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;

@UtilityClass
public final class ConsoleUtil {

    public static void sendPrefixPluginMessage(String prefix, String message) {
        if (prefix == null) {
            throw new NullPointerException("You cannot send prefix plugin message without prefix!");
        }

        sendPluginMessage(prefix + " " + message);
    }

    public static void sendPluginMessage(String message) {
        Bukkit.getConsoleSender().sendMessage(ColourUtil.colorize(message));
    }

}