package de.progen_bot.db.dao.playlist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.progen_bot.db.connection.ConnectionFactory;
import de.progen_bot.db.dao.Dao;

public class PlaylistSongDaoImpl extends Dao implements PlaylistSongDao {

    @Override
    public void generateTables(String sqlQuery) {
        final String sqlString = "CREATE TABLE IF NOT EXISTS songs (`id` INT(11) NOT NULL AUTO_INCREMENT, "
                + "`playlistId` INT(11) NOT NULL, `url` VARCHAR(100) NOT NULL, PRIMARY KEY( id ), "
                + "FOREIGN KEY (playlistId) REFERENCES playlist ( id ) ON DELETE CASCADE) ENGINE = INNODB;";
        super.generateTables(sqlString);
    }

    @Override
    public void addSong(String playlistId, String url) throws SQLException {
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement ps = connection.prepareStatement("INSERT INTO `songs` (`playlistId`, `url`) VALUES (?, ?);");
        ps.setString(1, playlistId);
        ps.setString(2, url);
        ps.execute();
    }

    @Override
    public void removeSong(String playlistId, String url) throws SQLException {
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement ps = connection.prepareStatement("DELETE FROM `songs` WHERE `playlistId` = ? AND `url` = ?");
        ps.setString(1, playlistId);
        ps.setString(2, url);
        ps.execute();
    }

    @Override
    public List<String> getSongsByPlaylist(String playlistId) throws SQLException {
        Connection connection = ConnectionFactory.getConnection();
        List<String> songs = new ArrayList<>();

        PreparedStatement ps = connection.prepareStatement("SELECT url FROM `songs` WHERE `playlistid` = ?;");
        ps.setString(1, playlistId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            songs.add(rs.getString("url"));
        }
        return songs;
    }

    public void addSongs(String playlistId, List<String> newUris) throws SQLException {
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement ps = connection.prepareStatement("INSERT INTO `songs` (`playlistId`, `url`) VALUES (?, ?);");

        newUris.forEach(url -> {
            try {
                ps.setString(1, playlistId);
                ps.setString(2, url);
                ps.addBatch();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        ps.executeBatch();
    }
}
