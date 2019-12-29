package de.progen_bot.db.dao.warnlist;

import de.progen_bot.db.connection.ConnectionFactory;
import de.progen_bot.db.dao.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WarnListDaoImpl extends Dao implements WarnListDao {
    final private String sqlQuery = "CREATE TABLE IF NOT EXISTS warn(`id` INT(11) NOT NULL AUTO_INCREMENT, `guildid` " +
            "VARCHAR(50) NOT NULL, `userid` VARCHAR(50) NOT NULL, \"" +
            "`reason` VARCHAR(50) NOT NULL, PRIMARY KEY(`id`) ) ENGINE = InnoDB DEFAULT CHARSET = utf8";

    private final String sqlQueryCount = "CREATE TABLE IF NOT EXISTS reportcount ( `userid` VARCHAR(50) NOT " +
            "NULL, " +
            "`count` INT(11) NOT NULL, PRIMARY KEY(`userid`) ) ENGINE = InnoDB DEFAULT CHARSET = utf8";

    public void insertWarn(String username, String reason) {
        Connection connection = ConnectionFactory.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO `warn` (userid,reason) VALUES(?,?)");
            ps.setString(1, username);
            ps.setString(2, reason);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Insert warn count.
     *
     * @param username the user id
     * @param count    the count
     */
    public void insertWarnCount(String username, int count) {
        Connection connection = ConnectionFactory.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("REPLACE INTO `reportcount` (userid,count) VALUES(?,?)");
            ps.setString(1, username);
            ps.setInt(2, count);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load warn count.
     *
     * @param username the user id
     * @return the int
     */
    public int loadWarnCount(String username) {
        Connection connection = ConnectionFactory.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM `reportcount` WHERE `userid` = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(2);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<String> loadWarnList(String userId) {
        Connection connection = ConnectionFactory.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM `warn` WHERE `userid` = ?");
            ps.setString(1, userId);

            ResultSet rs = ps.executeQuery();
            List<String> warnTable = new ArrayList<>();

            while (rs.next()) {
                warnTable.add(rs.getString(3));
            }
            return warnTable;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void generateTables(String query) {
        super.generateTables(sqlQuery);
        super.generateTables(sqlQueryCount);
    }
}
