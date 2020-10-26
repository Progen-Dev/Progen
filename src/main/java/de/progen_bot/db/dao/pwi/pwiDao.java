package de.progen_bot.db.dao.pwi;


import de.progen_bot.db.entities.config.GuildConfiguration;
import net.dv8tion.jda.api.entities.Guild;

public interface pwiDao{
    void writeConfig(GuildConfiguration configuration, Guild guild);
    GuildConfiguration loadConfig(Guild guild);
}