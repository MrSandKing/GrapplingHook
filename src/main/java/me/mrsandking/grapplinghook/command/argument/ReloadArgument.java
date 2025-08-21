package me.mrsandking.grapplinghook.command.argument;

import me.mrsandking.grapplinghook.GrapplingHookMain;
import me.mrsandking.grapplinghook.api.command.ArgumentCommand;
import me.mrsandking.grapplinghook.api.command.ArgumentInfo;
import me.mrsandking.grapplinghook.api.util.ColourUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

@ArgumentInfo(description = "reload configuration", permission = "grapplinghook.admin")
public final class ReloadArgument implements ArgumentCommand {

    @Override
    public boolean execute(CommandSender commandSender, String[] args) {
        GrapplingHookMain.getInstance().getGrapplingHookManager().load(GrapplingHookMain.getInstance());
        commandSender.sendMessage(ColourUtil.adjustColour(ChatColor.GREEN, "Configuration reloaded!"));
        return true;
    }
}