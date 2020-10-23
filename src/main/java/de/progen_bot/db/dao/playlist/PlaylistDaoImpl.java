package de.progen_bot.db.dao.playlist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import de.progen_bot.db.connection.ConnectionFactory;
import de.progen_bot.db.dao.Dao;
import de.progen_bot.db.entities.PlaylistData;
import net.dv8tion.jda.api.entities.User;

public class PlaylistDaoImpl extends Dao implements PlaylistDao {

    @Override
    public void generateTables(String sqlQuery) {

        String sqlString = "CREATE TABLE IF NOT EXISTS playlist (`id` INT(11) NOT NULL AUTO_INCREMENT, "
                + "`userid` VARCHAR(18) NOT NULL, `name` VARCHAR(30) NOT NULL, "
                + "PRIMARY KEY( id ), UNIQUE (userid, name)) ENGINE = INNODB;";
        super.generateTables(sqlString);
    }

    @Override
    public List<PlaylistData> getPlaylistsByUser(User user) throws SQLException {
        Connection connection = ConnectionFactory.getConnection();

        List<PlaylistData> playlists = new ArrayList<>();

        PreparedStatement ps = connection.prepareStatement("SELECT * FROM `playlist` WHERE `userid` = ?;");
        ps.setString(1, user.getId());
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            playlists.add(new PlaylistData(rs.getString("id"), rs.getString("name")));
        }

        return playlists;
    }

    @Override
    public void savePlaylist(List<String> uris, User user, String name) {
        Connection connection = ConnectionFactory.getConnection();
        for (String uri : uris) {
            try {
                PreparedStatement ps = connection
                        .prepareStatement("INSERT INTO `playlist` (`userid`, `name`, " + "`uri`) VALUES (?, ?, ?);");
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

        PreparedStatement ps = connection.prepareStatement("DELETE FROM `playlist` WHERE `userid` = ? AND `name` = ?;");
        ps.setString(1, user.getId());
        ps.setString(2, name);
        ps.execute();
    }

    public String createPlaylist(User user, String playlistName) throws SQLException {
        Connection connection = ConnectionFactory.getConnection();

        PreparedStatement ps = connection.prepareStatement("INSERT INTO `playlist` (`userid`, `name`) VALUES (?, ?);",
                Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, user.getId());
        ps.setString(2, playlistName);
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        rs.next();

        return rs.getString(1);
    }

    @Override
    public String getPlaylistByUserAndName(User user, String playlistName) throws SQLException {
        Connection connection = ConnectionFactory.getConnection();

        PreparedStatement ps = connection
                .prepareStatement("SELECT `id` FROM `playlist` WHERE `userid` = ? AND `name` = ?;");
        ps.setString(1, user.getId());
        ps.setString(2, playlistName);

        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getString(1);
    }
}
