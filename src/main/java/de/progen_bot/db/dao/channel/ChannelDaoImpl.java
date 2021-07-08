package de.progen_bot.db.dao.channel;

import de.progen_bot.db.dao.Dao;
import de.progen_bot.db.entities.channel.ChannelBuilder;
import de.progen_bot.db.entities.channel.ChannelData;
import net.dv8tion.jda.api.entities.Guild;
import de.progen_bot.db.connection.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ChannelDaoImpl extends Dao implements ChannelDao{
    @Override
    public void addLogChannelId(ChannelData channelData , Guild guild) {
        Connection connection = ConnectionFactory.getConnection();

        try {
            PreparedStatement ps = connection.prepareStatement("REPLACE INTO `channels` (`guildId`, `logChannelId`"
                    + ") VALUES (?,?);");
            ps.setString(1, guild.getId());
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ChannelData loadLogChannelId(ChannelData channelData , Guild guild) {
        Connection connection = ConnectionFactory.getConnection();

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM `channels` WHERE `guildId` =? AND `logChannelId` =?;");
            ps.setString(1, guild.getId());
            ps.setString(2, guild.getTextChannels().get(0).getId());
            ResultSet rs = ps.executeQuery();

            if (rs.next()){
                return new ChannelBuilder()
                        .setLogChannelId(rs.getString("logChannelId"))
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteLogChannelId(ChannelData channelData , Guild guild) {
        Connection connection = ConnectionFactory.getConnection();

        try {
            PreparedStatement ps = connection.prepareStatement(
                    "DELETE FROM `channels` WHERE `guildId` =? AND `logChannelId` =?;"
            );
            ps.setString(1, guild.getId());
            ps.setString(2, guild.getTextChannels().get(0).getId());
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void addStarBoardChannelId(ChannelData channelData , Guild guild) {
        Connection connection = ConnectionFactory.getConnection();

        try {
            PreparedStatement ps = connection.prepareStatement("REPLACE INTO `channels` (`guildId`, `starBoardChannelId`"
                    + ") VALUES (?,?);");
            ps.setString(1, guild.getId());
            ps.setString(2, guild.getTextChannels().get(0).getId());
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ChannelData loadStarBoardChannelId(ChannelData channelData , Guild guild) {
        Connection connection = ConnectionFactory.getConnection();

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM `channels` WHERE `guildId` =? AND `starBoardChannelId` =?;");
            ps.setString(1, guild.getId());
            ps.setString(2, guild.getTextChannels().get(0).getId());
            ResultSet rs = ps.executeQuery();

            if (rs.next()){
                return new ChannelBuilder()
                        .setStarBoardChannelId(rs.getString("starBoardChannelId"))
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteStarBoardChannelId(ChannelData channelData , Guild guild) {
        Connection connection = ConnectionFactory.getConnection();

        try {
            PreparedStatement ps = connection.prepareStatement(
                    "DELETE FROM `channels` WHERE `guildId` =? AND `starBoardChannelId` =?;"
            );
            ps.setString(1, guild.getId());
            ps.setString(2, guild.getTextChannels().get(0).getId());
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addVoiceLogChannelId(ChannelData channelData , Guild guild) {
        Connection connection = ConnectionFactory.getConnection();

        try {
            PreparedStatement ps = connection.prepareStatement("REPLACE INTO `channels` (`guildId`, `voiceLogChannelId`"
                    + ") VALUES (?,?);");
            ps.setString(1, guild.getId());
            ps.setString(2, guild.getTextChannels().get(0).getId());
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ChannelData loadVoiceLogChannelId(ChannelData channelData , Guild guild) {
        Connection connection = ConnectionFactory.getConnection();

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM `channels` WHERE `guildId` =? AND `voiceLogChannelId` =?;");
            ps.setString(1, guild.getId());
            ps.setString(2, guild.getTextChannels().get(0).getId());
            ResultSet rs = ps.executeQuery();

            if (rs.next()){
                return new ChannelBuilder()
                        .setVoiceLogChannelId(rs.getString("voiceLogChannelId"))
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteVoiceLogChannelId(ChannelData channelData , Guild guild) {
        Connection connection = ConnectionFactory.getConnection();

        try {
            PreparedStatement ps = connection.prepareStatement(
                    "DELETE FROM `channels` WHERE `guildId` =? AND `voiceLogChannelId` =?;"
            );
            ps.setString(1, guild.getId());
            ps.setString(2, guild.getTextChannels().get(0).getId());
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void generateTables(String query){
        String sqlQuery = "CREATE TABLE IF NOT EXISTS channels (guildId VARCHAR(18) NOT NULL, logChannelId VARCHAR(18), starBoardChannelId VARCHAR(18), voiceLogChannelId VARCHAR(18)) ENGINE = InnoDB;";
        super.generateTables(sqlQuery);
    }
}
