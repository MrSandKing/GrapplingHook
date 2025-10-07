package me.mrsandking.grapplinghook.api.util;

import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@UtilityClass
public final class ColourUtil {

    /**
     * This function returns colorized string (including hex colours).
     * @param string - text to be colorized
     * @return colorized text
     */

    public static String colorize(String string) {
        if(string == null)
            return null;
        string = ChatColor.translateAlternateColorCodes('&', string); // Translates any & codes too
        return string;
    }

    /**
     * This function applies colour to text.
     * @param color - to be applied
     * @param text - text to adjust colour
     * @return coloured text
     */

    public static String adjustColour(ChatColor color, String text) {
        return color + text;
    }

    public static List<String> colouredLore(String... lore) {
        return (lore != null) ? Arrays.stream(lore).map(ColourUtil::colorize).collect(Collectors.toList()) : new ArrayList<>();
    }

    public static List<String> colouredLore(List<String> lore) {
        return (lore != null) ? lore.stream().filter(Objects::nonNull).map(ColourUtil::colorize).collect(Collectors.toList()) : new ArrayList<>();
    }

    public static List<String> colouredLore(String lore) {
        List<String> list = new ArrayList<>();
        String[] strings = lore.split("\n");
        for(String s : strings)
            list.add(colorize(s));
        return list;
    }

    /**
     * This method returns coloured string array.
     * @param list - list to be coloured
     * @return coloured string array
     */

    public static String[] asColouredArray(List<String> list) {
        return (list != null) ? list.stream().filter(Objects::nonNull).map(ColourUtil::colorize).toArray(String[]::new) : new String[0];
    }

    public static String[] colouredArray(List<String> list, String[] placeholders, Object... objects) {
        if (placeholders.length != objects.length) {
            return colouredLore(list).toArray(String[]::new);
        }

        String[] colouredArray = colouredLore(list).toArray(String[]::new);

        for (String s : colouredArray) {
            for (int x = 0; x < placeholders.length; x++) {
                s = s.replace(placeholders[x], String.valueOf(objects[x]));
            }
        }

        return colouredArray;
    }

}