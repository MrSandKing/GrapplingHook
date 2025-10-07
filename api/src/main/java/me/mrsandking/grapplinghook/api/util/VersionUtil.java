package me.mrsandking.grapplinghook.api.util;

import org.bukkit.Bukkit;

public final class VersionUtil {

    public static String getVersion() {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        return packageName.substring(packageName.lastIndexOf(".") + 1);
    }

}