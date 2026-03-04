package io.hearlov.nexus.perms.Group;

import java.util.Arrays;

public class Group{

    public final String name;
    private final String nameTag;
    private final String chatFormat;
    private final String[] permissions;

    public Group(String name, String nameTag, String chatFormat, String[] permissions){
        this.name = name;
        this.nameTag = nameTag;
        this.chatFormat = chatFormat;
        this.permissions = permissions;
    }

    public String getName(){
        return name;
    }

    public String getNameTag(){
        return nameTag;
    }

    public String getChatFormat(){
        return chatFormat;
    }

    public String[] getPermissions(){
        return permissions;
    }

}
