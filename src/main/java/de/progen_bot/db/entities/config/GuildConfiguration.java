package de.progen_bot.db.entities.config;

import net.dv8tion.jda.api.entities.Guild;

public class GuildConfiguration {

    public String prefix;
    public final String logChannelID;
    public String tempChannelCategoryID;
    public final String setAutorole;

    GuildConfiguration(String prefix, String logChannelID, String tempChannelCategoryID, String setAutoRole) {

        this.prefix = prefix;
        this.logChannelID = logChannelID;
        this.tempChannelCategoryID = tempChannelCategoryID;
        this.setAutorole = setAutoRole;
    }

    public void setAutorole(String role, Guild guild) {
    }
}
