package io.hearlov.nexus.perms.Util;

import cn.nukkit.Player;
import io.hearlov.nexus.perms.Cache.PlayerCache;
import io.hearlov.nexus.perms.Group.Group;

import java.util.HashMap;
import java.util.Map;

public class StringReplacer{

    public static String getFormatted(Player player, String message){
        String format = PlayerCache.getGroup(player.getName()).getChatFormat();

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("{player}", player.getName());
        placeholders.put("{msg}", message);

        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            format = format.replace(entry.getKey(), entry.getValue());
        }

        return format;
    }

    public static String getFormattedNameTag(Player player){
        String format = PlayerCache.getGroup(player.getName()).getNameTag();

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("{player}", player.getName());

        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            format = format.replace(entry.getKey(), entry.getValue());
        }

        return format;
    }

    public static String getFormattedNameTag(Player player, Group group){
        String format = group.getNameTag();

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("{player}", player.getName());

        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            format = format.replace(entry.getKey(), entry.getValue());
        }

        return format;
    }

}
