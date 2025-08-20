/**
 * Every command might have its sub-commands. This interface helps
 * in organising and coding new functionalities to commands.
 */

package me.mrsandking.grapplinghook.api.command;

import org.bukkit.command.CommandSender;

import java.util.LinkedList;
import java.util.List;

public interface ArgumentCommand {

    /**
     * This is the execute function - it does what it is called.
     * Any given instruction will execute in this method.
     * @param commandSender - the given command sender, it is either player or console
     * @param args - the array of all arguments
     * @return boolean value
     */

    boolean execute(CommandSender commandSender, String[] args);

    /**
     * Every sub-command may use this function to declare arguments in it.
     * @return list of arguments - default list is empty.
     */

    default List<String> getArguments() {
        return new LinkedList<>();
    }

}