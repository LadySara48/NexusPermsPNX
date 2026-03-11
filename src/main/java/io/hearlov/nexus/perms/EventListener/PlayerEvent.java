package io.hearlov.nexus.perms.EventListener;

import cn.nukkit.Player;
import cn.nukkit.event.Listener;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.player.PlayerLoginEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import io.hearlov.nexus.db.cache.DBType;
import io.hearlov.nexus.perms.Cache.PlayerCache;
import io.hearlov.nexus.perms.NexusPerms;

import cn.nukkit.event.player.PlayerChatEvent;
import io.hearlov.nexus.perms.Util.StringReplacer;

@SuppressWarnings("unused")
public class PlayerEvent implements Listener{

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        if(NexusPerms.getInstance().getDb().dbType == DBType.MySQL)
            PlayerCache.getPermissionCache().remove(event.getPlayer().getName());
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event){
        PlayerCache.registerPlayer(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(PlayerChatEvent event){
        if(event.isCancelled()) return;
        event.setCancelled();

        String message = event.getMessage();
        Player player = event.getPlayer();

        String msg = StringReplacer.getFormatted(player, message);
        player.getServer().broadcastMessage(msg);
    }

}
