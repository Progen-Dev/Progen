package de.progen_bot.db.entities.config;

public class GuildConfiguration {

    public String prefix;
    public String logChannelID;
    public String tempChannelCategoryID;

    GuildConfiguration(String prefix, String logChannelID, String tempChannelCategoryID) {

        this.prefix = prefix;
        this.logChannelID = logChannelID;
        this.tempChannelCategoryID = tempChannelCategoryID;
    }
}
