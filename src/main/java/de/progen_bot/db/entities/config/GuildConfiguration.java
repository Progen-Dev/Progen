package de.progen_bot.db.entities.config;

public class GuildConfiguration {

    private String prefix;
    private String logChannelID;
    private String tempChannelCategoryID;
    private String autoRole;

    GuildConfiguration(String prefix, String logChannelID, String tempChannelCategoryID, String autoRole) {

        this.prefix = prefix;
        this.logChannelID = logChannelID;
        this.tempChannelCategoryID = tempChannelCategoryID;
        this.autoRole = autoRole;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getLogChannelID() {
        return logChannelID;
    }

    public void setLogChannelID(String logChannelID) {
        this.logChannelID = logChannelID;
    }

    public String getTempChannelCategoryID() {
        return tempChannelCategoryID;
    }

    public void setTempChannelCategoryID(String tempChannelCategoryID) {
        this.tempChannelCategoryID = tempChannelCategoryID;
    }

    public String getAutoRole() {
        return autoRole;
    }

    public void setAutoRole(String autoRole) {
        this.autoRole = autoRole;
    }
}
