package io.hearlov.nexus.perms.Cache;

import cn.nukkit.utils.Config;
import io.hearlov.nexus.perms.Group.Group;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GroupCache{

    private static Map<String, Group> groupCache;

    public static void setup(Config cfg){
        groupCache = new ConcurrentHashMap<>();

        for(String key : cfg.getKeys(false)){
            String nametag = cfg.getString(key+".NameTag", "");
            String chatformat = cfg.getString(key+".ChatFormat", "");
            String[] permissions = cfg.getStringList(key+".Permissions").toArray(new String[0]);

            groupCache.put(key, new Group(key, nametag, chatformat, permissions));
        }
        System.out.println(groupCache);
    }

    public static Group getGroup(@Nullable String key){
        if(key == null){
            return getDefaultGroup();
        }

        if(!groupCache.containsKey(key)){
            return getDefaultGroup();
        }

        return groupCache.get(key);
    }

    public static Group getGroupAsNullable(@NotNull String key){
        return groupCache.get(key);
    }

    public static Group getDefaultGroup(){
        return groupCache.get("Default");
    }

    @SuppressWarnings("unused")
    public static Map<String, Group> getGroupCache() {
        return groupCache;
    }
}
