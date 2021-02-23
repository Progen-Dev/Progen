package de.progen_bot.database.dao.config;

import de.progen_bot.database.dao.Dao;
import de.progen_bot.database.entities.config.GuildConfiguration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConfigDaoImpl extends Dao implements ConfigDao
{
    @Override
    public String generateTables()
    {
        return "CREATE TABLE IF NOT EXISTS config (guildid VARCHAR(18) NOT NULL, prefix VARCHAR " +
                "(18) NOT NULL , logChannelId VARCHAR(18), tempChannelCategoryId VARCHAR(18), autorole VARCHAR(18), UNIQUE " +
                "(guildid)) ENGINE = InnoDB;";
    }

    @Override
    public void writeConfig(GuildConfiguration configuration, long guildId)
    {
        try
        {
            final PreparedStatement ps = connection.prepareStatement("REPLACE INTO `config` (`guildid`, `prefix`, " +
                    "`logChannelId`, `tempChannelCategoryId`, `autorole`) VALUES (?, ?, ?, ?, ?);");

            ps.setString(1, String.valueOf(guildId));
            ps.setString(2, configuration.getPrefix());
            ps.setString(3, String.valueOf(configuration.getLogChannelId()));
            ps.setString(4, String.valueOf(configuration.getTempChannelCategoryId()));
            ps.setString(5, String.valueOf(configuration.getAutorole()));

            ps.execute();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public GuildConfiguration loadConfig(long guildId)
    {
        try
        {
            final PreparedStatement ps = connection.prepareStatement("SELECT * FROM `config` WHERE `guildid` = ?;");

            ps.setString(1, String.valueOf(guildId));

            final ResultSet rs = ps.executeQuery();

            if (rs.next())
                return new GuildConfiguration.Builder()
                        .setPrefix(rs.getString("prefix"))
                        .setLogChannelId(Long.parseLong(rs.getString("logChannelId")))
                        .setTempChannelCategoryId(Long.parseLong(rs.getString("tempChannelCategoryId")))
                        .setAutorole(Long.parseLong(rs.getString("autorole")))
                        .build();
            else
            {
                final GuildConfiguration configuration = new GuildConfiguration.Builder().build();

                this.writeConfig(configuration, guildId);

                return configuration;
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
