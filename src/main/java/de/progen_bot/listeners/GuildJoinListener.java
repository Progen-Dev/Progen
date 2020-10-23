package de.progen_bot.listeners;

import javax.annotation.Nonnull;

import de.progen_bot.db.dao.config.ConfigDaoImpl;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.db.entities.config.GuildConfigurationBuilder;
import de.progen_bot.util.Settings;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * Class to set GuildConfiguration for a new guild onGuildJoin.
 */
public class GuildJoinListener extends ListenerAdapter {

    @Override
    public void onGuildJoin(@Nonnull GuildJoinEvent event) {

        GuildConfiguration guildConfiguration = new ConfigDaoImpl().loadConfig(event.getGuild());

        if (guildConfiguration == null) {
            guildConfiguration = new GuildConfigurationBuilder().setLogChannelID(null).setPrefix(Settings.PREFIX)
                    .setTempChannelCategoryID(null).build();

            new ConfigDaoImpl().writeConfig(guildConfiguration, event.getGuild());
        }
    }
}
