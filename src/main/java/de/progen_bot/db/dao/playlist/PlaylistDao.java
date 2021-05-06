package de.progen_bot.db.dao.playlist;

import java.sql.SQLException;
import java.util.List;

import de.progen_bot.db.entities.PlaylistData;
import net.dv8tion.jda.api.entities.User;

public interface PlaylistDao {

    List<PlaylistData> getPlaylistsByUser(User user) throws SQLException;

    String getPlaylistByUserAndName(User user, String playlistName) throws SQLException;

    void savePlaylist(List<String> uris, User user, String name);

    void deletePlaylist(User user, String name) throws SQLException;

}
