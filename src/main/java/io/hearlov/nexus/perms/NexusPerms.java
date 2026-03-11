package io.hearlov.nexus.perms;

import cn.nukkit.command.Command;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;

import io.hearlov.nexus.db.Main;
import io.hearlov.nexus.db.cache.NexusDB;
import io.hearlov.nexus.perms.Cache.GroupCache;
import io.hearlov.nexus.perms.Commands.GiveGroupCommand;
import io.hearlov.nexus.perms.EventListener.PlayerEvent;
import io.hearlov.nexus.perms.Util.Query;

import java.util.ArrayList;
import java.util.List;

public class NexusPerms extends PluginBase{

    private static NexusPerms instance;
    private NexusDB db;

    public static NexusPerms getInstance() { return instance; }
    @Override public void onLoad() { instance = this; }

    @Override
    public void onEnable(){
        this.saveDefaultConfig();
        this.saveResource("groups.yml", false);
        Config tagconfig = new Config(getDataFolder().getPath() + "/groups.yml", Config.YAML);

        String uri = getDataFolder().getAbsolutePath().replace("\\", "/");

        String dbmode = getConfig().getString("DBMode");
        if(dbmode.equalsIgnoreCase("H2")) this.db = Main.getInstance().createH2("NexusPerms H2 DB", uri + "/nexusperms");
        else if(dbmode.equalsIgnoreCase("MySQL")){
            String host = getConfig().getString("MySQL.Host");
            int port = getConfig().getInt("MySQL.Port");
            String username = getConfig().getString("MySQL.Username");
            String password = getConfig().getString("MySQL.Password");
            String database = getConfig().getString("MySQL.Database");
            this.db = Main.getInstance().createSQL("NexusPerms MySQL DB", host, port, username, password, database);
        }

        db.addTask(Query.query, null, null);

        init();
        GroupCache.setup(tagconfig);
        //this.getServer().getScheduler().scheduleDelayedRepeatingTask(new TickQueueTask(), 100, 15);
    }

    public NexusDB getDb(){
        return db;
    }

    public void init(){
        this.getServer().getPluginManager().registerEvents(new PlayerEvent(), this);

        List<Command> commands = new ArrayList<>();
        commands.add(new GiveGroupCommand());

        this.getServer().getCommandMap().registerAll("", commands);
    }
}