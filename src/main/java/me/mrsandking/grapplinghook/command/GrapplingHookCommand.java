package me.mrsandking.grapplinghook.command;

import me.mrsandking.grapplinghook.GrapplingHookMain;
import me.mrsandking.grapplinghook.api.util.ColourUtil;
import me.mrsandking.grapplinghook.inventory.GrapplingHooksInventory;
import me.mrsandking.grapplinghook.object.GrapplingHookImpl;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public final class GrapplingHookCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player player)) {
            return false;
        }

        if (strings.length == 0) {
            new GrapplingHooksInventory().open(player);
            return true;
        }

        if (strings.length == 1) {
            String arg = strings[0];
            if (arg.equalsIgnoreCase("reset")) {
                GrapplingHookImpl.SERIAL_NUMBER = 0;
                GrapplingHookMain.getInstance().getConfig().set("SerialNumber", GrapplingHookImpl.SERIAL_NUMBER);
                GrapplingHookMain.getInstance().saveConfig();

                player.sendMessage(ColourUtil.adjustColour(ChatColor.GREEN, "Serial numbers have been cleared and set to 0."));
                return true;
            }
        }

        if (strings.length == 3) {

        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return List.of();
    }
}