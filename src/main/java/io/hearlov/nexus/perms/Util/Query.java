package io.hearlov.nexus.perms.Util;

public class Query{

    public static final String query = "CREATE TABLE IF NOT EXISTS nexusperms (\n" +
            "    name VARCHAR(255) PRIMARY KEY,\n" +
            "    `group` VARCHAR(255) NOT NULL,\n" +
            "    endtms BIGINT NOT NULL\n" +
            ")";

    public static final String getPlayerGroup = "SELECT * FROM nexusperms WHERE name = ?";

    public static final String mergeGroup = "MERGE INTO nexusperms (name, `group`, endtms) VALUES (?, ?, ?)";

}
