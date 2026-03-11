package io.hearlov.nexus.perms.Commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import io.hearlov.nexus.perms.Cache.GroupCache;
import io.hearlov.nexus.perms.Cache.PlayerCache;
import io.hearlov.nexus.perms.Group.Group;

public class GiveGroupCommand extends Command{

    public GiveGroupCommand(){
        super("givegroup", "Give group", "/givegroup (player) (group)", new String[]{"gig"});
        this.setPermission("nexus.perms.givegroup");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args){
        if(args.length != 2) return false;

        Player p = sender.getServer().getPlayer(args[0]);
        Group group = GroupCache.getGroupAsNullable(args[1]);

        if (group == null) return false;
        if(p == null){
            PlayerCache.setGroup(args[0], group, 0);
        }else{
            PlayerCache.setGroup(p, group, 0);
        }
        return true;
    }


}