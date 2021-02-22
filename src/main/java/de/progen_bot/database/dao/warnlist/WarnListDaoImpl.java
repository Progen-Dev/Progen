package de.progen_bot.database.dao.warnlist;

import de.progen_bot.database.connection.ConnectionFactory;
import de.progen_bot.database.dao.Dao;
import net.dv8tion.jda.api.entities.Member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WarnListDaoImpl extends Dao implements WarnListDao
{
    private final Connection connection = ConnectionFactory.getConnection();
    // TODO: 20.02.2021
    @Override
    public String generateTables()
    {
        return "CREATE TABLE IF NOT EXISTS warn(`id` INT(11) NOT NULL AUTO_INCREMENT, `guildid` "
                + "VARCHAR(50) NOT NULL, `userid` VARCHAR(50) NOT NULL, "
                + "`reason` VARCHAR(50) NOT NULL, PRIMARY KEY(`id`) ) ENGINE = InnoDB DEFAULT CHARSET = utf8";
    }

    @Override
    public void insertWarn(Member member, String reason)
    {
        try
        {
            final PreparedStatement ps = this.connection.prepareStatement("INSERT INTO `warn` (userid, reason, guilid) VALUES (?, ?, ?);");

            ps.setString(1, member.getId());
            ps.setString(2, reason);
            ps.setString(3, member.getGuild().getId());

            ps.execute();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> loadWarnList(Member member)
    {
        final List<String> warns = new ArrayList<>();

        try
        {
            final PreparedStatement ps = this.connection.prepareStatement("SELECT * FROM `warn` WHERE `userid` = ? AND `guildid = ?;");

            ps.setString(1, member.getId());
            ps.setString(2, member.getGuild().getId());

            final ResultSet rs = ps.executeQuery();

            while (rs.next())
                warns.add(rs.getString("reason"));
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return warns;
    }

    @Override
    public void deleteWarn(Member member, String reason)
    {
        try {
            final PreparedStatement ps = this.connection.prepareStatement("DELETE FROM `warn` WHERE `userid` = ? AND `guildid` = ? AND `reason` = ?;");

            ps.setString(1, member.getId());
            ps.setString(2, member.getGuild().getId());
            ps.setString(3, reason);

            ps.execute();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
