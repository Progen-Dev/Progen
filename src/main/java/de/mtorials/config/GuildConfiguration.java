package de.mtorials.config;

import net.dv8tion.jda.api.entities.Guild;

public class GuildConfiguration {

    public String prefix;
    public String logChannelID;
    public String tempChannelCatergoryID;

    GuildConfiguration(String prefix, String logChannelID, String tempChannelCatergoryID) {

        this.prefix = prefix;
        this.logChannelID = logChannelID;
        this.tempChannelCatergoryID = tempChannelCatergoryID;
    }
}
