package de.progen_bot.database.dao.config;

import de.progen_bot.database.entities.config.GuildConfiguration;

public interface ConfigDao
{
    void writeConfig(GuildConfiguration configuration, long guildId);
    GuildConfiguration loadConfig(long guildId);
}
