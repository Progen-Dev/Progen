package de.progen_bot.database.dao.poll;

import de.progen_bot.core.Main;
import de.progen_bot.database.dao.Dao;
import de.progen_bot.database.entities.poll.PollData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

// TODO: 22.02.2021 infinite amount of options
public class PollDaoImpl extends Dao implements PollDao
{
    @Override
    public void savePollData(PollData data)
    {
        try
        {
            PreparedStatement ps = connection.prepareStatement(
                    "REPLACE INTO `poll` (pollid,userid,messageid,users,open,option1,option2,option3,option4,option5,"
                            + "option6,option7,option8,option9,time,channelId) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            ps.setInt(1, data.getPollId());
            ps.setString(2, data.getUserId());
            ps.setString(3, data.getMessageId());
            ps.setString(4, data.getUsers());
            ps.setBoolean(5, data.isOpen());

            for (int i = 6; i <= 14; i++)
            {
                if (data.getOptions() != null)
                {
                    ps.setInt(i, data.getOptions()[i - 6]);
                }
                else
                {
                    ps.setInt(i, 0);
                }
            }
            ps.setTimestamp(15, Timestamp.valueOf(data.getTime()));
            ps.setString(16, data.getChannelId());
            ps.execute();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public PollData getPollData(String messageId)
    {
        try
        {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM `poll` WHERE `messageid` = ?");
            ps.setString(1, messageId);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
            {
                return (setPollDataFromRS(rs));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public void loadPollTimer()
    {
        try
        {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM `poll`");
            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                PollData data = setPollDataFromRS(rs);
                if (data.getTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                        - LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() > 0
                        && data.isOpen())
                {
                    // FIXME: 22.02.2021 wtf
                    if (Main.getJDA().getTextChannelById(data.getChannelId()) != null)
                    {
                        Main.getJDA().getTextChannelById(data.getChannelId());
                    }
                }
                else if (data.isOpen())
                {
                    data.setOpen(false);

                    final TextChannel channel = Main.getJDA().getTextChannelById(data.getChannelId());
                    if (channel != null)
                    {
                        channel.sendMessage(
                                new EmbedBuilder().setColor(Color.blue).setDescription("Poll closed!").build()).queue();
                    }
                    PollData.saveToDb(data);
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    private static PollData setPollDataFromRS(ResultSet rs)
    {
        PollData data = new PollData();

        try
        {
            data.setPollId(rs.getInt(1));
            data.setUserId(rs.getString(2));
            data.setMessageId(rs.getString(3));
            data.setUsers(rs.getString(4));
            data.setOpen(rs.getBoolean(5));

            int[] options = new int[9];

            for (int i = 0; i <= 8; i++)
            {
                options[i] = rs.getInt(i + 6);
            }

            data.setOptions(options);
            data.setTime(rs.getTimestamp(15).toLocalDateTime());
            data.setChannelId(rs.getString(16));
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    public String generateTables()
    {
        return "CREATE TABLE IF NOT EXISTS poll ( `pollid` INT(11) NOT NULL AUTO_INCREMENT, "
                + "`userid` VARCHAR(50) NOT NULL, `messageid` VARCHAR(50) NOT NULL, `users` TEXT(1000),`open` BOOLEAN NOT NULL,"
                + "`option1` VARCHAR(50) NOT NULL,`option2` VARCHAR(50) NOT NULL,`option3` VARCHAR(50) NOT NULL,"
                + "`option4` VARCHAR(50) NOT NULL,`option5` VARCHAR(50) NOT NULL,`option6` VARCHAR(50) NOT NULL,"
                + "`option7` VARCHAR(50) NOT NULL,`option8` VARCHAR(50) NOT NULL,`option9` VARCHAR(50) NOT NULL,"
                + "`time` TIMESTAMP NOT NULL,`channelId` VARCHAR(50) NOT NULL, PRIMARY KEY(`pollid`) ) ENGINE = InnoDB "
                + "DEFAULT CHARSET = utf8";
    }
}
