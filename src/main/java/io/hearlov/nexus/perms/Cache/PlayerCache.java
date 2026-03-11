package io.hearlov.nexus.perms.Cache;

import cn.nukkit.Player;
import io.hearlov.nexus.perms.Group.Group;
import io.hearlov.nexus.perms.NexusPerms;

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

        NexusPerms.getInstance().getDb().addTask("MERGE INTO nexusperms (name, `group`, endtms) VALUES (?, ?, ?)", new Object[]{player.getName(), group.getName(), endtms}, null);
        permissionCache.put(player.getName(), group);
        if(endtms != 0) timeCache.put(player.getName(), endtms);

        PlayerAttachments.reloadAttachments(player);
    }

    public static void setGroup(String playerName, Group group, int endtime){
        long endtms = endtime <= 0 ? 0 : ((System.currentTimeMillis() / 1000) + endtime);

        NexusPerms.getInstance().getDb().addTask("MERGE INTO nexusperms (name, `group`, endtms) VALUES (?, ?, ?)", new Object[]{playerName, group.getName(), endtms}, null);
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
        NexusPerms.getInstance().getDb().addTask("SELECT * FROM nexusperms WHERE name = ?", new Object[]{player.getName()}, (List<Map<String, Object>> rs) -> {
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
                }else{
                    permissionCache.putAll(cch.get());
                    addPlayerAttachments(entry.getKey(), entry.getValue());
                }
            }
        });
    }

    public static void addPlayerAttachments(String name, Group permissions){
        Player player = NexusPerms.getInstance().getServer().getPlayer(name);
        if(player == null) return;
        PlayerAttachments.playerAttachmentsAdd(player, permissions);
    }

    /*@SuppressWarnings("unused")
    public static void tickQueue(){ //Old MainThreadPool
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
