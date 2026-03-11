package io.hearlov.nexus.perms.Cache;

import cn.nukkit.Player;
import io.hearlov.nexus.perms.Group.Group;
import io.hearlov.nexus.perms.NexusPerms;
import io.hearlov.nexus.perms.Util.Query;
import io.hearlov.nexus.perms.Util.StringReplacer;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

public class PlayerCache{

    //private static final Queue<Map<String, Group>> queue = new ConcurrentLinkedQueue<>();
    public static final Map<String, Group> permissionCache = new ConcurrentHashMap<>();
    public static final Map<String, Long> timeCache = new ConcurrentHashMap<>();

    public static Group getPermissions(String playerName){
        return permissionCache.get(playerName);
    }

    public static Map<String, Group> getPermissionCache(){
        return permissionCache;
    }

    public static void setGroup(Player player, Group group, int endtime){
        long endtms = endtime <= 0 ? 0 : ((System.currentTimeMillis() / 1000) + endtime);

        NexusPerms.getInstance().getDb().addTask(Query.mergeGroup, new Object[]{player.getName(), group.getName(), endtms}, null);
        permissionCache.put(player.getName(), group);
        if(endtms != 0) timeCache.put(player.getName(), endtms);

        PlayerAttachments.reloadAttachments(player);
    }

    public static void setGroup(String playerName, Group group, int endtime){
        long endtms = endtime <= 0 ? 0 : ((System.currentTimeMillis() / 1000) + endtime);

        NexusPerms.getInstance().getDb().addTask(Query.mergeGroup, new Object[]{playerName, group.getName(), endtms}, null);
        permissionCache.put(playerName, group);
        if(endtms != 0) timeCache.put(playerName, endtms);

        Player player = NexusPerms.getInstance().getServer().getPlayer(playerName);
        if(player != null) PlayerAttachments.reloadAttachments(player);
    }

    public static Group getGroup(String playerName){
        return permissionCache.get(playerName);
    }

    public static void registerPlayer(Player player){
        AtomicReference<Map<String, Group>> cch = new AtomicReference<>();
        NexusPerms.getInstance().getDb().addTask(Query.getPlayerGroup, new Object[]{player.getName()}, (List<Map<String, Object>> rs) -> {
            Map<String, Group> map = new HashMap<>();
            if(!rs.isEmpty()){
                Map<String, Object> obj = rs.getFirst();
                Long endtms = (Long) obj.get("ENDTMS");

                if(endtms != 0 && endtms < System.currentTimeMillis() / 1000){
                    map.put(player.getName(), null);
                    cch.set(map);
                    return;
                }

                String group = (String) obj.get("GROUP");
                Group grp = GroupCache.getGroup(group);
                if(endtms != 0) timeCache.put(player.getName(), endtms);
                map.put(player.getName(), grp);
            }else{
                map.put(player.getName(), null);
            }

            cch.set(map);
        }, (rs) -> { //New NexusDB MainThreadPool
            for(Map.Entry<String, Group> entry : cch.get().entrySet()){
                if(entry.getValue() == null){
                    setGroup(entry.getKey(), GroupCache.getDefaultGroup(), 0);
                    setPlayerNameTag(entry.getKey(), GroupCache.getDefaultGroup());
                }else{
                    permissionCache.putAll(cch.get());
                    addPlayerAttachments(entry.getKey(), entry.getValue());
                    setPlayerNameTag(entry.getKey(), entry.getValue());
                }
            }
        });
    }

    public static void addPlayerAttachments(@NotNull String name, @NotNull Group permissions){
        Player player = NexusPerms.getInstance().getServer().getPlayer(name);
        if(player == null) return;
        PlayerAttachments.playerAttachmentsAdd(player, permissions);
    }

    public static void setPlayerNameTag(@NotNull String name, @NotNull Group group){
        Player player = NexusPerms.getInstance().getServer().getPlayer(name);
        if(player == null) return;
        player.setNameTag(StringReplacer.getFormattedNameTag(player, group));
    }

    /*public static void tickQueue(){
        while(!queue.isEmpty()){

            Map<String, Group> poll = queue.poll();

            for(Map.Entry<String, Group> entry : poll.entrySet()){
                System.out.println(entry);
                if(entry.getValue() == null){
                    setGroup(entry.getKey(), GroupCache.getDefaultGroup(), 0);
                }else{
                    permissionCache.putAll(poll);
                    addPlayerAttachments(entry.getKey(), entry.getValue());
                }
            }

        }
    }*/

}
