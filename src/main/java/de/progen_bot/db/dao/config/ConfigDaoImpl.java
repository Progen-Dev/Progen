package de.progen_bot.db.dao.config;

import de.progen_bot.db.connection.ConnectionFactory;
import de.progen_bot.db.dao.Dao;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.db.entities.config.GuildConfigurationBuilder;
import net.dv8tion.jda.api.entities.Guild;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConfigDaoImpl extends Dao implements ConfigDao {

	@Override
    public void writeConfig(GuildConfiguration configuration, Guild guild) {
        Connection connection = ConnectionFactory.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("REPLACE INTO `config` (`guildid`, `prefix`, " +
                    "`logChannelID`, `tempChannelCategoryID`, `autorole`) VALUES (?, ?, ?, ?, ?);");
            ps.setString(1, guild.getId());
            ps.setString(2, configuration.getPrefix());
            ps.setString(3, configuration.getLogChannelID());
            ps.setString(4, configuration.getTempChannelCategoryID());
            ps.setString(5, configuration.getAutoRole());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public GuildConfiguration loadConfig(Guild guild) {
        Connection connection = ConnectionFactory.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM `config` WHERE `guildid` = ?;");
            ps.setString(1, guild.getId());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new GuildConfigurationBuilder().setPrefix(rs.getString("prefix"))
                        .setLogChannelID(rs.getString("logChannelID"))
                        .setTempChannelCategoryID(rs.getString("tempChannelCategoryID"))
                        .setAutorole(rs.getString("autorole")).build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void generateTables(String query) {
		String sqlQuery = "CREATE TABLE IF NOT EXISTS config (guildid VARCHAR(18) NOT NULL, prefix VARCHAR" +
				"(18) NOT NULL , logChannelID VARCHAR(18), tempChannelCategoryID VARCHAR(18), autorole VARCHAR(18), UNIQUE " +
				"(guildid)) ENGINE = InnoDB;";
		super.generateTables(sqlQuery);
    }
}
