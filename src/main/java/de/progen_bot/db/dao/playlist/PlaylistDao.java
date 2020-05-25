package de.progen_bot.db.dao.playlist;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import net.dv8tion.jda.api.entities.User;

public interface PlaylistDao {

    HashMap<String, List<String>> getPlaylistsByUser(User user) throws SQLException;
    void savePlaylist(List<String> uris, User user, String name);
    void deletePlaylist(User user, String name) throws SQLException;

}
