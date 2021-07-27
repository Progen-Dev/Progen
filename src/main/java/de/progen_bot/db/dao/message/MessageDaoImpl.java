package de.progen_bot.db.dao.message;

import de.progen_bot.db.connection.ConnectionFactory;
import de.progen_bot.db.dao.Dao;
import net.dv8tion.jda.api.entities.TextChannel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessageDaoImpl extends Dao implements MessageDao{
    @Override
    public void addWelcomeMessage(TextChannel channel , String message) {
        Connection connection = ConnectionFactory.getConnection();

        try {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO `messages` (guildId, welcomeMessage, welcomeChannelId) VALUES(?,?,?)"
            );
            ps.setString(1, channel.getGuild().getId());
            ps.setString(2, channel.getAsMention());
            ps.setString(3, message);
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> loadWelcomeMessage(TextChannel channel) {
        List<String> wemsg = new ArrayList<>();
        Connection connection = ConnectionFactory.getConnection();
        
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM `messages` WHERE `guildId` = ? AND `welcomeChannelId` =?"
            );
            ps.setString(1, channel.getGuild().getId());
            ps.setString(2, channel.getAsMention());
            ResultSet rs = ps.executeQuery();
            ps.execute();
            
            while (rs.next()){
                wemsg.add(rs.getString("welcomeMessage"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wemsg;
    }

    @Override
    public void deleteWelcomeMessage(TextChannel channel, String message) {
        Connection connection = ConnectionFactory.getConnection();

        try {
            PreparedStatement ps = connection.prepareStatement(
                    "DELETE FROM `messages` WHERE `guildId` =? AND `welcomeChannelId` =? AND `welcomeMessage` =?"
            );
            ps.setString(1, channel.getGuild().getId());
            ps.setString(2, channel.getAsMention());
            ps.setString(3, message);
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void generateTables(String query){
        String sqlQuery = "CREATE TABLE IF NOT EXISTS messages(`id` INT(11) NOT NULL AUTO_INCREMENT, `guildId`"
                + " VARCHAR(50) NOT NULL, `welcomeChannelId` VARCHAR(50) NOT NULL, " 
                + "`welcomeMessage` VARCHAR(50) NOT NULL,"
                + "PRIMARY KEY(`id`) "
                + ") ENGINE = InnoDB DEFAULT CHARSET = utf8";
        super.generateTables(sqlQuery);
    }
}
