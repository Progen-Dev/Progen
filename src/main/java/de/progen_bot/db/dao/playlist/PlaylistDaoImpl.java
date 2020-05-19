package de.progen_bot.db.dao.playlist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.progen_bot.db.connection.ConnectionFactory;
import de.progen_bot.db.dao.Dao;
import net.dv8tion.jda.api.entities.User;

public class PlaylistDaoImpl extends Dao implements PlaylistDao{

    private final String sqlString = "CREATE TABLE IF NOT EXISTS playlist (`id` INT(11) NOT NULL AUTO_INCREMENT KEY, `userid` VARCHAR(18) NOT NULL, `playlistname` VARCHAR(30) NOT NULL, uri VARCHAR(100) NOT NULL) ENGINE = INNODB;";

    @Override
    public void generateTables(String sqlQuery) {

        super.generateTables(this.sqlString);
    }

    @Override
    public HashMap<String, List<String>> getPlaylistsByUser(User user) throws SQLException {
        Connection connection = ConnectionFactory.getConnection();
        HashMap<String, List<String>> map = new HashMap<>();

            PreparedStatement ps = connection.prepareStatement("SELECT * FROM `playlist` WHERE `userid` = ?;");
            ps.setString(1, user.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String playlistname = rs.getString("playlistname");
                String uri = rs.getString("uri");
                if (!map.containsKey(playlistname)) map.put(playlistname, new ArrayList<>());
                map.get(playlistname).add(uri);
            }

        return map;
    }

    @Override
    public void savePlaylist(List<String> uris, User user, String name) {
        Connection connection = ConnectionFactory.getConnection();
        for (String uri : uris) {
            try {
                PreparedStatement ps = connection.prepareStatement("INSERT INTO `playlist` (`userid`, `playlistname`, " +
                        "`uri`) VALUES (?, ?, ?);");
                ps.setString(1, user.getId());
                ps.setString(2, name);
                ps.setString(3, uri);
                ps.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void deletePlaylist(User user, String name) throws SQLException {
        Connection connection = ConnectionFactory.getConnection();

                PreparedStatement ps = connection.prepareStatement("DELETE FROM `playlist` WHERE `userid` = ? AND `playlistname` = ?;");
                ps.setString(1, user.getId());
                ps.setString(2, name);
                ps.execute();

    }
}
