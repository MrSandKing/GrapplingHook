package me.mrsandking.grapplinghook.command.argument;

import me.mrsandking.grapplinghook.GrapplingHookMain;
import me.mrsandking.grapplinghook.api.command.ArgumentCommand;
import me.mrsandking.grapplinghook.api.command.ArgumentInfo;
import me.mrsandking.grapplinghook.api.util.ColourUtil;
import me.mrsandking.grapplinghook.object.GrapplingHookImpl;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

@ArgumentInfo(description = "use this to reset serial numbers", permission = "grapplinghook.admin")
public final class ResetArgument implements ArgumentCommand {

    @Override
    public boolean execute(CommandSender commandSender, String[] args) {
        GrapplingHookImpl.SERIAL_NUMBER = 0;
        GrapplingHookMain.getInstance().getConfig().set("SerialNumber", GrapplingHookImpl.SERIAL_NUMBER);
        GrapplingHookMain.getInstance().saveConfig();

        commandSender.sendMessage(ColourUtil.adjustColour(ChatColor.GREEN, "Serial numbers have been cleared and set to 0."));
        return true;
    }

}