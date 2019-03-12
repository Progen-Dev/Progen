package de.mtorials.config;

import java.util.ArrayList;

public class GuildConfigurationBuilder {

    private String preifx;
    private String logChannelID;
    private ArrayList<String> permissionGroup1RoleNames = new ArrayList<>();
    private String tempChannelCatergoryID;

    public GuildConfigurationBuilder setPrefix(String prefix) {
        this.preifx = prefix;
        return this;
    }

    public GuildConfigurationBuilder setLogChannelID(String logChannelID) {
        this.logChannelID = logChannelID;
        return this;
    }

    public GuildConfigurationBuilder addPermissionGroup1RoleNames(String permissionGroup1RoleNames) {
        this.permissionGroup1RoleNames.add(permissionGroup1RoleNames);
        return this;
    }

    public GuildConfigurationBuilder setTempChannelCatergoryID(String tempChannelCatergoryID) {
        this.tempChannelCatergoryID = tempChannelCatergoryID;
        return this;
    }

    public GuildConfiguration build() {
        return new GuildConfiguration(this.preifx, this.logChannelID, (String[])this.permissionGroup1RoleNames.toArray(), this.tempChannelCatergoryID);
    }
}
