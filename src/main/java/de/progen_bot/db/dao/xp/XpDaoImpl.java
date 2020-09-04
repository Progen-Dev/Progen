package de.progen_bot.db.dao.xp;

import de.progen_bot.db.connection.ConnectionFactory;
import de.progen_bot.db.dao.Dao;
import de.progen_bot.db.entities.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class XpDaoImpl extends Dao implements XpDao {

	@Override
    public void saveUserData(UserData data) {
        Connection connection = ConnectionFactory.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("REPLACE INTO `xp` (id,userid,totalxp," +
                    "level,notify) VALUES(?,?,?,?,?)");

            ps.setInt(1, data.getId());
            ps.setString(2, data.getUserId());
            ps.setLong(3, data.getTotalXp());
            ps.setLong(4, data.getLevel());
            ps.setBoolean(5, data.getLvlUpNotify());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public UserData loadFromId(String userId) {
        Connection connection = ConnectionFactory.getConnection();
        UserData data = new UserData();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM `xp` WHERE `userid` = ?");
            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                data.setId(rs.getInt(1));
                data.setUserId(rs.getString(2));
                data.setDBTotalXp(rs.getLong(3));
                data.setDBLevel(rs.getInt(4));
                data.setLvlUpNotify(rs.getBoolean(5));
                return data;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<String> getTop10Ranks() {
        Connection connection = ConnectionFactory.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM `xp` ORDER BY `totalxp` DESC LIMIT 10");
            ResultSet rs = ps.executeQuery();
            List<String> top10 = new ArrayList<>();
            while (rs.next()) {
                top10.add(rs.getString(2));
            }
            return top10;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void generateTables(String query) {
		String sqlQuery = "CREATE TABLE IF NOT EXISTS xp(`id` INT(11) NOT NULL AUTO_INCREMENT, `userid` " +
				"VARCHAR(50) NOT NULL, `totalxp` BIGINT(12) NOT NULL, `level` INT(11) NOT NULL, `notify` BOOLEAN NOT NULL, "
				+ "PRIMARY KEY(`id`) ) ENGINE = InnoDB DEFAULT CHARSET = utf8";
		super.generateTables(sqlQuery);
    }
}
