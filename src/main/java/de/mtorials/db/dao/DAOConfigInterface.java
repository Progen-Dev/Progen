package de.mtorials.db.dao;

import de.mtorials.config.GuildConfiguration;
import net.dv8tion.jda.api.entities.Guild;

public interface DAOConfigInterface {

    void writeConfig(GuildConfiguration configuration, Guild guild);
    GuildConfiguration loadConfig(Guild guild);
}
