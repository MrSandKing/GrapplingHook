package me.mrsandking.grapplinghook.command;

import me.mrsandking.grapplinghook.api.command.ArgumentCommand;
import me.mrsandking.grapplinghook.api.command.ArgumentInfo;
import me.mrsandking.grapplinghook.api.command.BaseCommand;
import me.mrsandking.grapplinghook.api.util.ColourUtil;
import me.mrsandking.grapplinghook.command.argument.GiveArgument;
import me.mrsandking.grapplinghook.command.argument.HooksArgument;
import me.mrsandking.grapplinghook.command.argument.ReloadArgument;
import me.mrsandking.grapplinghook.command.argument.ResetArgument;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class CommandHandler extends BaseCommand {

    public CommandHandler() {
        super("grapplinghook");

        setAliases(List.of("gh", "ghook"));
    }

    /**
     * Handles the execution of admin commands for Stop It Slender.
     *
     * @param commandSender The entity (player or console) that sent the command.
     * @param s The alias used for the command.
     * @param strings The arguments passed to the command.
     * @return true if the command is executed successfully, otherwise false.
     */

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 0) {
            commandSender.sendMessage(ColourUtil.colorize("&6Help for Grappling Hook:"));
            for (Map.Entry<String, ArgumentCommand> entry : getArguments().entrySet()) {
                ArgumentInfo argumentInfo = entry.getValue().getClass().getAnnotation(ArgumentInfo.class);
                if (commandSender.hasPermission(argumentInfo.permission()) || commandSender.isOp()) {
                    final String line = ColourUtil.adjustColour(ChatColor.GREEN, "/gh " + entry.getKey() + " - " + argumentInfo.description());
                    commandSender.sendMessage(line);
                }
            }
            return false;
        }

        if (!getArguments().containsKey(strings[0])) {
            commandSender.sendMessage("Argument does not exist!");
            return false;
        }

        ArgumentCommand argumentCommand = getArguments().get(strings[0]);
        ArgumentInfo argumentInfo = argumentCommand.getClass().getAnnotation(ArgumentInfo.class);

        if (argumentInfo.useByPlayer() && !(commandSender instanceof Player)) {
            commandSender.sendMessage("Only player may perform this command!");
            return false;
        }

        if (!commandSender.hasPermission(argumentInfo.permission())) {
            commandSender.sendMessage("You do not have permission to use this command!");
            return false;
        }

        if(strings.length > 1 && !argumentCommand.getArguments().contains(strings[1]) /*&& argumentCommand.hasArguments() && !argumentCommand.flexibleArguments()*/) {
            commandSender.sendMessage("This does not contain any arguments!");
            return false;
        }

        argumentCommand.execute(commandSender, strings);
        return true;
    }

    /**
     * Registers the subcommands for the admin commands of the Grappling Hook.
     * <p>
     * This method is called during the construction of the command handler
     * to set up all the available subcommands that administrators can use.
     * <p>
     * Each subcommand is linked to a specific ArgumentCommand class, which
     * defines its behavior and permissions.
     */

    @Override
    protected void registerArguments() {
        // Admin subcommands
        registerCommand("give", GiveArgument.class);
        registerCommand("hooks", HooksArgument.class);
        registerCommand("reload", ReloadArgument.class);
        registerCommand("reset", ResetArgument.class);
    }

}