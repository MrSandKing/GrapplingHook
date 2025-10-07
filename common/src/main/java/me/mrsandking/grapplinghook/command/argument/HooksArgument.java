package me.mrsandking.grapplinghook.command.argument;

import me.mrsandking.grapplinghook.api.command.ArgumentCommand;
import me.mrsandking.grapplinghook.api.command.ArgumentInfo;
import me.mrsandking.grapplinghook.inventory.GrapplingHooksInventory;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@ArgumentInfo(description = "open inventory of grappling hooks", useByPlayer = true, permission = "grapplinghook.admin")
public final class HooksArgument implements ArgumentCommand {

    @Override
    public boolean execute(CommandSender commandSender, String[] args) {
        Player player = (Player) commandSender;
        new GrapplingHooksInventory().open(player);
        return true;
    }
}