package me.superchirok1.unluckycraft;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorUtils {

    public static String translateHex(String message) {
        if (message == null) return "";
        final Pattern hexPattern = Pattern.compile("&#([A-Fa-f0-9]{6})");
        Matcher matcher = hexPattern.matcher(message);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            String hex = matcher.group(1);
            matcher.appendReplacement(buffer, ChatColor.of("#" + hex).toString());
        }
        matcher.appendTail(buffer);
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', buffer.toString());
    }



}
