package io.hearlov.nexus.perms.Cache;

import cn.nukkit.Player;
import cn.nukkit.permission.PermissionAttachment;
import io.hearlov.nexus.perms.Group.Group;
import io.hearlov.nexus.perms.NexusPerms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerAttachments{

    private static final Map<String, List<PermissionAttachment>> perms = new HashMap<>();

    public static void playerAttachmentsAdd(Player player, Group group){
        List<PermissionAttachment> permAtt = new ArrayList<>();
            for(String perms : group.getPermissions()){
                PermissionAttachment attachment = player.addAttachment(NexusPerms.getInstance(), perms, true);
                permAtt.add(attachment);
        }

        perms.put(player.getName(), permAtt);
    }

    public static void playerAttackmentsRemove(Player player){
        if(!perms.containsKey(player.getName())) return;
        for(PermissionAttachment permissionAttachment : perms.get(player.getName())){
            player.removeAttachment(permissionAttachment);
        }
    }

    public static void reloadAttachments(Player player){
        if(!perms.containsKey(player.getName())) return;
        playerAttackmentsRemove(player);
        playerAttachmentsAdd(player, PlayerCache.getPermissions(player.getName()));
    }

}
