package de.progen_bot.db.dao.messages;

import de.progen_bot.db.connection.ConnectionFactory;
import de.progen_bot.db.dao.Dao;
import de.progen_bot.db.entities.MessageBuilder;
import de.progen_bot.db.entities.MessageData;
import net.dv8tion.jda.api.entities.Guild;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageDaoImpl extends Dao implements MessageDao {
    @Override
    public void addWelcomeMessage(MessageData messageData , Guild guild) {
        Connection connection = ConnectionFactory.getConnection();

        try {
            PreparedStatement ps = connection.prepareStatement("REPLACE INTO `messages` (`guildId`, `textChannelId`, `welcomeMessage`" +
                    ") VALUES (?,?,?);");
            ps.setString(1, guild.getId());
            ps.setString(2, guild.getTextChannels().get(0).getId());
            ps.setString(3, messageData.getWelcomeMessage());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public MessageData loadWelcomeConfig(Guild guild) {
        Connection connection = ConnectionFactory.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM `messages` WHERE `guildId` = ?;");
            ps.setString(1, guild.getId());
            ResultSet rs = ps.executeQuery();

            if (rs.next()){
                return new MessageBuilder()
                        .setWelcomeMessage(rs.getString("welcomeMessage"))
                        .setWelcomeMessageChannel(rs.getString("welcomeMessageChannel"))
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addLeaveMessage(MessageData messageData , Guild guild) {
        Connection connection = ConnectionFactory.getConnection();

        try {
            PreparedStatement ps = connection.prepareStatement("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public MessageData loadLeaveConfig(Guild guild) {
        return null;
    }

    @Override
    public void generateTables(String query){
        String sqlQuery = "CREATE TABLE IF NOT EXISTS messages (guildid VARCHAR(18) NOT NULL, textChannelId VARCHAR(18) NOT NULL, welcomeMessage VARCHAR" +
                "(18), welcomeMessageChannelId VARCHAR(18), leaveMessage VARCHAR(18), leaveMessageChannelId VARCHAR (18)) ENGINE = InnoDB;";
        super.generateTables(sqlQuery);
    }
}
