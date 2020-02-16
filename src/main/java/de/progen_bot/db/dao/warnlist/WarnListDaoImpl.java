package de.progen_bot.db.dao.warnlist;

import de.progen_bot.db.connection.ConnectionFactory;
import de.progen_bot.db.dao.Dao;
import net.dv8tion.jda.api.entities.Member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WarnListDaoImpl extends Dao implements WarnListDao {
    final private String sqlQuery = "CREATE TABLE IF NOT EXISTS warn(`id` INT(11) NOT NULL AUTO_INCREMENT, `guildid` " +
            "VARCHAR(50) NOT NULL, `userid` VARCHAR(50) NOT NULL, " +
            "`reason` VARCHAR(50) NOT NULL, PRIMARY KEY(`id`) ) ENGINE = InnoDB DEFAULT CHARSET = utf8";

    public void insertWarn(Member member, String reason) {
        Connection connection = ConnectionFactory.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO `warn` (userid , reason, guildid) VALUES(?,?,?)");
            ps.setString(1, member.getUser().getId());
            ps.setString(2, reason);
            ps.setString(3, member.getGuild().getId());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> loadWarnList(Member member) {
        List<String> warns = new ArrayList<>();
        Connection connection = ConnectionFactory.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM `warn` WHERE `userid` = ? AND `guildid` = ?");
            ps.setString(1, member.getUser().getId());
            ps.setString(2, member.getGuild().getId());

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                warns.add(rs.getString("reason"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return warns;
    }

    public void deleteWarns(Member member) {
        Connection connection = ConnectionFactory.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM `warn` WHERE `userid` = ? AND `guildid` = ?");
            ps.setString(1, member.getUser().getId());
            ps.setString(2, member.getGuild().getId());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void generateTables(String query) {
        super.generateTables(sqlQuery);
    }
}
