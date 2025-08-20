package me.mrsandking.grapplinghook.api.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.File;
import java.util.Random;

@UtilityClass
public final class Util {

    public static void sendPrefixPluginMessage(String prefix, String message) {
        if (prefix == null) {
            throw new NullPointerException("You cannot send prefix plugin message without prefix!");
        }

        sendPluginMessage(prefix + " " + message);
    }

    public static void sendPluginMessage(String message) {
        Bukkit.getConsoleSender().sendMessage(ColourUtil.colorize(message));
    }


    private static final Random random = new Random();

    public static void createFile(File file) {
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) { }
        }
    }

    public static Location getStringLocation(String location, boolean precised) {
        if(location == null) return null;
        try {
            String[] params = location.split(":");
            if(precised) {
                double x = Double.parseDouble(params[0]);
                double y = Double.parseDouble(params[1]);
                double z = Double.parseDouble(params[2]);
                double pitch = Double.parseDouble(params[3]);
                double yaw  = Double.parseDouble(params[4]);
                String world = params[5];
                return new Location(Bukkit.getWorld(world),x,y,z, (float) pitch, (float) yaw);
            }
            int x = Integer.parseInt(params[0]);
            int y = Integer.parseInt(params[1]);
            int z = Integer.parseInt(params[2]);
            String world = params[3];
            return new Location(Bukkit.getWorld(world),x,y,z);
        } catch (Exception e) {}
        return null;
    }

    public static String getLocationString(Location location, boolean precised) {
        if(location == null) return null;
        if(precised)
            return location.getX()+":"+location.getY()+":"+location.getZ()+":"+location.getPitch()+":"+location.getYaw()+":"+location.getWorld().getName();
        return location.getBlockX()+":"+location.getBlockY()+":"+location.getBlockZ()+":"+location.getWorld().getName();
    }

    public static int getRandomNumber(int bound) {
        return random.nextInt(bound);
    }

}