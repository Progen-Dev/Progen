package de.progen_bot.db.entities.config;

public class GuildConfiguration {

    private String prefix;
    private String logChannelID;
    private String tempChannelCategoryID;
    private String autoRole;
    private String starBoardChannelID;

    GuildConfiguration(String prefix, String logChannelID, String tempChannelCategoryID, String autoRole, String starBoardChannelID) {

        this.prefix = prefix;
        this.logChannelID = logChannelID;
        this.tempChannelCategoryID = tempChannelCategoryID;
        this.autoRole = autoRole;
        this.starBoardChannelID = starBoardChannelID;
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

    public String getStarBoardChannelID(){
        return starBoardChannelID;
    }

    public void setStarBoardChannelID(String starBoardChannelID){
        this.starBoardChannelID = starBoardChannelID;
    }
}