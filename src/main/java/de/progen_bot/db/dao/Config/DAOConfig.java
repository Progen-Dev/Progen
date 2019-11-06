package de.progen_bot.db.dao.Config;

import de.mtorials.config.GuildConfiguration;
import de.mtorials.config.GuildConfigurationBuilder;
import de.mtorials.db.dao.DAO;
import de.mtorials.db.dao.DAOConfigInterface;
import de.mtorials.exceptions.GuildHasNoConfigException;
import net.dv8tion.jda.api.entities.Guild;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class DAOConfig extends DAO implements DAOConfigInterface {
    @Override
    public void generateTables() {
      super.getMySQLConnection().update("CREATE TABLE IF NOT EXISTS config (guildid VARCHAR(18) NOT NULL , prefix VARCHAR(18) NOT NULL , logChannelID VARCHAR(18) NOT NULL , tempChannelCatergoryID VARCHAR(18) NOT NULL, UNIQUE (id)) ENGINE = InnoDB;\");");
    }

    @Override
    public void writeConfig(GuildConfiguration configuration , Guild guild) {
        ResultSet rs = super.getMySQLConnection().query("SELECT * FROM 'config' WHERE 'guildid'");
            try {
                if (!rs.next()) throw new GuildHasNoConfigException();
                new GuildConfigurationBuilder().setLogChannelID(rs.getString("guildid"));
            }catch (SQLException e){
                e.printStackTrace();
            }
    }

    @Override
    public GuildConfiguration loadConfig(Guild guild) {
        return null;
    }
}
