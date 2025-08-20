package me.mrsandking.grapplinghook.api.command;

import lombok.Getter;
import me.mrsandking.grapplinghook.api.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Getter
public abstract class BaseCommand extends Command {

    private final Map<String, ArgumentCommand> arguments;

    protected BaseCommand(@NotNull String command) {
        super(command);
        this.arguments = new LinkedHashMap<>();

        this.registerArguments();
    }

    protected abstract void registerArguments();

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        List<String> completions = new ArrayList<>();
        if(args.length == 1) {
            StringUtil.copyPartialMatches(args[0], arguments.keySet(), completions);
            Collections.sort(completions);
            return completions;
        } else if(args.length == 2 && !getArgumentsForSubcommand(args[0]).isEmpty()) {
            return getArgumentsForSubcommand(args[0]);
        } else return Collections.emptyList();
    }

    public void registerCommand(String command, Class<? extends ArgumentCommand> clazz) {
        try {
            ArgumentCommand argumentCommand = clazz.getConstructor().newInstance();
            ArgumentInfo argumentInfo = clazz.getAnnotation(ArgumentInfo.class);

            arguments.put(command, argumentCommand);

            if (Bukkit.getPluginManager().getPermission(argumentInfo.permission()) == null) {
                Bukkit.getPluginManager().addPermission(new Permission(argumentInfo.permission()));
            }
        } catch (Exception e) {
            Util.sendPrefixPluginMessage("&6[Grappling Hook]", "Something went wrong while registering argument '" + command + "'!");
            Util.sendPrefixPluginMessage("&6[Grappling Hook]", "Exception message: " + e.getMessage());
        }
    }

    public List<String> getArgumentsForSubcommand(@NotNull String subcommand) {
        if (arguments.get(subcommand) == null) {
            return Collections.emptyList();
        }

        return arguments.get(subcommand).getArguments();
    }

}