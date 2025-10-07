package me.mrsandking.grapplinghook.command.argument;

import me.mrsandking.grapplinghook.GrapplingHookMain;
import me.mrsandking.grapplinghook.api.GrapplingHook;
import me.mrsandking.grapplinghook.api.GrapplingHookApi;
import me.mrsandking.grapplinghook.api.command.ArgumentCommand;
import me.mrsandking.grapplinghook.api.command.ArgumentInfo;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@ArgumentInfo(description = "give the argument", permission = "grapplinghook.admin")
public final class GiveArgument implements ArgumentCommand {

    // /ghook give <id> <player>

    @Override
    public boolean execute(CommandSender commandSender, String[] args) {
        if (args.length != 3) {
            commandSender.sendMessage("Not enough arguments...");
            return false;
        }

        final String id = args[1];

        if (!getArguments().contains(id)) {
            commandSender.sendMessage("This grappling hook does not exist!");
            return false;
        }

        final String string = args[2];
        final Player player = Bukkit.getPlayer(string);
        if (player == null) {
            commandSender.sendMessage("This target is not online!");
            return false;
        }

        GrapplingHook grapplingHook = GrapplingHookMain.getInstance().getGrapplingHookManager().getGrapplingHook(id);
        if (player.getInventory().firstEmpty() != -1) {
            player.getInventory().addItem(GrapplingHookApi.getInstance().getGrapplingHookInjector().createGrapplingHook(grapplingHook, player.getUniqueId()));
        }

        return true;
    }

    @Override
    public List<String> getArguments() {
        return GrapplingHookMain.getInstance()
                .getGrapplingHookManager()
                .getGrapplingHooks()
                .stream()
                .map(GrapplingHook::getId)
                .toList();
    }
}