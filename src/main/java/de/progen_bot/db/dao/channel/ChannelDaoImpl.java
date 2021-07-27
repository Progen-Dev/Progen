package de.progen_bot.db.dao.channel;

import de.progen_bot.db.connection.ConnectionFactory;
import de.progen_bot.db.dao.Dao;
import net.dv8tion.jda.api.entities.TextChannel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ChannelDaoImpl extends Dao implements ChannelDao {
    @Override
    public void addLogChannel(TextChannel channel) {
        Connection connection = ConnectionFactory.getConnection();

        try {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO `costumeChannel` (guildId, logChannelId) VALUES(?,?)");
            ps.setString(1 , channel.getGuild().getId());
            ps.setString(2 , channel.getId());
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> loadLogChannel(TextChannel channel) {
        List<String> logChannel = new ArrayList<>();
        Connection connection = ConnectionFactory.getConnection();

        try {
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM `costumeChannel` WHERE `guildId` = ? AND `logChannelId` = ?"
            );
            ps.setString(1, channel.getGuild().getId());
            ps.setString(2, channel.getId());

            ResultSet rs = ps.executeQuery();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return logChannel;
    }

    @Override
    public void deleteLogChannel(TextChannel channel) {
        Connection connection = ConnectionFactory.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "DELETE FROM `costumeChannel` WHERE `guildId` =? AND `logChannelId` = ?"
            );
            ps.setString(1, channel.getGuild().getId());
            ps.setString(2, channel.getId());
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addStarBoardChannel(TextChannel channel) {
        Connection connection = ConnectionFactory.getConnection();

        try {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO `costumeChannel` (guildId, starBordChannelId) VALUES(?,?)");
            ps.setString(1, channel.getGuild().getId());
            ps.setString(2, channel.getId());
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> loadStarBoardChannel(TextChannel channel) {
        List<String> starBoardChannel = new ArrayList<>();
        Connection connection = ConnectionFactory.getConnection();

        try {
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM `costumeChannel` WHERE `guildId` = ? AND `starBordChannelId` = ?"
            );
            ps.setString(1, channel.getGuild().getId());
            ps.setString(2, channel.getId());

            ResultSet rs = ps.executeQuery();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return starBoardChannel;
    }

    @Override
    public void deleteStarBoardChannel(TextChannel channel) {
        Connection connection = ConnectionFactory.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "DELETE FROM `costumeChannel` WHERE `guildId` =? AND `starBordChannelId` = ?"
            );
            ps.setString(1, channel.getGuild().getId());
            ps.setString(2, channel.getId());
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void generateTables(String query){
        String sqlQuery = "CREATE TABLE IF NOT EXISTS costumeChannel(`id` INT(11) NOT NULL AUTO_INCREMENT, `guildId` "
                + "VARCHAR(50),`logChannelId` VARCHAR(50), "
                + "`starBordChannelId` VARCHAR(50),"
                + "PRIMARY KEY(`id`) ) ENGINE = InnoDB DEFAULT CHARSET = utf8";
        super.generateTables(sqlQuery);
    }
}
