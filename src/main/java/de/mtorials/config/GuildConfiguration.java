package de.mtorials.config;

import net.dv8tion.jda.core.entities.Guild;


public class GuildConfiguration {

    private Guild guild;

    private String prefix;
    private String logChannelID;
    private String[] permissionGroup1RoleNames;
    private String tempChannelCatergoryID;

    GuildConfiguration() {}

    GuildConfiguration(String prefix, String logChannelID, String tempChannelCatergoryID) {

        this.prefix = prefix;
        this.logChannelID = logChannelID;
        this.permissionGroup1RoleNames = permissionGroup1RoleNames;
        this.tempChannelCatergoryID = tempChannelCatergoryID;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getLogChannelID() {
        return logChannelID;
    }

    public String[] getPermissionGroup1RoleNames() {
        return permissionGroup1RoleNames;
    }

    public String getTempChannelCatergoryID() {
        return tempChannelCatergoryID;
    }
}
