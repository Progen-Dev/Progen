package de.progen_bot.db.dao.playlist;

import java.sql.SQLException;
import java.util.List;

public interface PlaylistSongDao {
    void addSong(String playlistId, String url) throws SQLException;

    void removeSong(String playlistId, String url) throws SQLException;

    List<String> getSongsByPlaylist(String playlistId) throws SQLException;
}
