package de.mtorials.config;

import net.dv8tion.jda.api.entities.Guild;


public class GuildConfiguration {

    private Guild guild;

    private String prefix;
    private String logChannelID;
    private String tempChannelCatergoryID;

    GuildConfiguration() {
    }

    GuildConfiguration(String prefix, String logChannelID, String tempChannelCatergoryID) {

        this.prefix = prefix;
        this.logChannelID = logChannelID;
        this.tempChannelCatergoryID = tempChannelCatergoryID;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getLogChannelID() {
        return logChannelID;
    }

    public String getTempChannelCatergoryID() {
        return tempChannelCatergoryID;
    }
}
