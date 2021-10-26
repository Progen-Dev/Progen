package de.progen_bot.commands.music;

import java.awt.Color;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.core.Main;
import de.progen_bot.db.dao.playlist.PlaylistDaoImpl;
import de.progen_bot.db.dao.playlist.PlaylistSongDaoImpl;
import de.progen_bot.db.entities.PlaylistData;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.music.AudioInfo;
import de.progen_bot.music.Music;
import de.progen_bot.music.MusicManager;
import de.progen_bot.permissions.AccessLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandPlaylist extends CommandHandler {

    public CommandPlaylist() {
        super("playlist",
                "`playlist create <playlist name>` creates a new playlist.\n"
                        + "`playlist queue <playlist name>` creates a new playlist from the current queue.\n"
                        + "`playlist delete <playlist name>` deletes the playlist.\n"
                        + "`playlist play <playlist name>` loads a previously saved playlist in the queue.\n"
                        + "`playlist list` lists all your playlists.\n"
                        + "`playlist add <playlist name> <song url>` adds a song to your playlist.\n"
                        + "`playlist remove <playlist name> <song url>` removes a song from your playlist.\n"
                        + "`playlist songs <playlist name>` lists all songs in your playlist.\n",
                "Handles Playlists! Playlists are globally available. That means you can use them on every guild with Progen!");
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event,
            GuildConfiguration configuration) {

        final Member member = event.getMember();

        if (member == null)
            return;

        if (parsedCommand.getArgs().length < 1) {
            event.getTextChannel().sendMessageEmbeds(super.messageGenerators.generateErrorMsgWrongInput()).queue();
            return;
        }

        Music music;
        String playlistId;
        switch (parsedCommand.getArgsAsList().get(0)) {
            case "create": // Create new playlist
                if (parsedCommand.getArgs().length < 2) {
                    event.getTextChannel().sendMessageEmbeds(super.messageGenerators.generateErrorMsgWrongInput()).queue();
                    return;
                }

                try {
                    new PlaylistDaoImpl().createPlaylist(member.getUser(), parsedCommand.getArgsAsList().get(1));
                } catch (SQLIntegrityConstraintViolationException e1) {
                    event.getTextChannel().sendMessageEmbeds(super.messageGenerators
                            .generateErrorMsg("A playlist with this name already exists. Please choose another name."))
                            .queue();
                    break;
                } catch (SQLException e2) {
                    e2.printStackTrace();
                }

                event.getTextChannel()
                        .sendMessageEmbeds(super.messageGenerators.generateInfoMsg(
                                "Successfully created the playlist `" + parsedCommand.getArgsAsList().get(1) + "`!"))
                        .queue();
                break;
            case "queue": // Add current queue to playlist

                if (checkForMusic(event))
                    return;

                music = Main.getMusicManager().getMusicByChannel(member.getVoiceState().getChannel());

                if (parsedCommand.getArgs().length < 2) {
                    event.getTextChannel().sendMessageEmbeds(super.messageGenerators.generateErrorMsgWrongInput()).queue();
                    return;
                }

                List<String> newUris = new ArrayList<>();
                newUris.add(music.getPlayer().getPlayingTrack().getInfo().uri);
                for (AudioInfo info : music.getManager().getQueue()) {
                    newUris.add(info.getTrack().getInfo().uri);
                }

                try {
                    playlistId = new PlaylistDaoImpl().createPlaylist(member.getUser(),
                            parsedCommand.getArgsAsList().get(1));

                    new PlaylistSongDaoImpl().addSongs(playlistId, newUris);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                event.getTextChannel()
                        .sendMessageEmbeds(super.messageGenerators.generateInfoMsg("Successfully created the playlist `"
                                + parsedCommand.getArgsAsList().get(1) + "` from the current queue!"))
                        .queue();
                break;

            case "play":
            case "p":
                if (checkForMusic(event))
                    return;

                music = Main.getMusicManager().getMusicByChannel(member.getVoiceState().getChannel());

                if (parsedCommand.getArgs().length < 2) {
                    event.getTextChannel().sendMessageEmbeds(super.messageGenerators.generateErrorMsgWrongInput()).queue();
                    return;
                }

                List<String> uris;
                try {
                    playlistId = new PlaylistDaoImpl().getPlaylistByUserAndName(event.getAuthor(),
                            parsedCommand.getArgsAsList().get(1));
                    uris = new PlaylistSongDaoImpl().getSongsByPlaylist(playlistId);
                } catch (SQLException e) {
                    event.getTextChannel()
                            .sendMessageEmbeds(super.messageGenerators.generateErrorMsg("Could not find playlist!")).queue();
                    return;
                }
                music.getManager().purgeQueue();
                for (String uri : uris) {
                    music.loadTrack(uri, member);
                }

                event.getTextChannel()
                        .sendMessageEmbeds(
                                super.messageGenerators.generateInfoMsg("Playlist is loaded in queue after this song!"))
                        .queue();
                break;

            case "delete": // delete playlist
                if (parsedCommand.getArgs().length < 2) {
                    event.getTextChannel().sendMessageEmbeds(super.messageGenerators.generateErrorMsgWrongInput()).queue();
                    return;
                }

                try {
                    new PlaylistDaoImpl().getPlaylistByUserAndName(event.getAuthor(),
                            parsedCommand.getArgsAsList().get(1));
                    new PlaylistDaoImpl().deletePlaylist(event.getAuthor(), parsedCommand.getArgsAsList().get(1));
                    event.getTextChannel().sendMessageEmbeds(super.messageGenerators.generateInfoMsg(
                            "Successfully removed the playlist `" + parsedCommand.getArgsAsList().get(1) + "`!"))
                            .queue();
                } catch (SQLException e) {
                    e.printStackTrace();
                    event.getTextChannel()
                            .sendMessageEmbeds(super.messageGenerators.generateErrorMsg("Could not find playlist!")).queue();
                }
                break;
            case "add": // Add a song
                if (parsedCommand.getArgs().length < 3) {
                    event.getTextChannel().sendMessageEmbeds(super.messageGenerators.generateErrorMsgWrongInput()).queue();
                    return;
                }

                if (!isValidURL(parsedCommand.getArgsAsList().get(2))) {
                    event.getTextChannel()
                            .sendMessageEmbeds(super.messageGenerators.generateErrorMsg("Please enter a valid song url."))
                            .queue();
                    return;
                }
                try {
                    // Get the playlist id
                    playlistId = new PlaylistDaoImpl().getPlaylistByUserAndName(member.getUser(),
                            parsedCommand.getArgsAsList().get(1));
                    new PlaylistSongDaoImpl().addSong(playlistId, parsedCommand.getArgsAsList().get(2));
                    event.getTextChannel()
                            .sendMessageEmbeds(super.messageGenerators
                                    .generateInfoMsg("Successfully added `" + parsedCommand.getArgsAsList().get(2)
                                            + "` to `" + parsedCommand.getArgsAsList().get(1) + "`!"))
                            .queue();
                } catch (SQLException e) {
                    e.printStackTrace();
                    event.getTextChannel()
                            .sendMessageEmbeds(super.messageGenerators.generateErrorMsg("Could not find playlist!")).queue();
                }
                break;
            case "remove": // Remove a song
                if (parsedCommand.getArgs().length < 3) {
                    event.getTextChannel().sendMessageEmbeds(super.messageGenerators.generateErrorMsgWrongInput()).queue();
                    return;
                }

                try {
                    // Get the playlist id
                    playlistId = new PlaylistDaoImpl().getPlaylistByUserAndName(event.getAuthor(),
                            parsedCommand.getArgsAsList().get(1));
                    new PlaylistSongDaoImpl().removeSong(playlistId, parsedCommand.getArgsAsList().get(2));
                    event.getTextChannel()
                            .sendMessageEmbeds(super.messageGenerators
                                    .generateInfoMsg("Successfully removed `" + parsedCommand.getArgsAsList().get(2)
                                            + "` from `" + parsedCommand.getArgsAsList().get(1) + "`!"))
                            .queue();
                } catch (SQLException e) {
                    event.getTextChannel()
                            .sendMessageEmbeds(super.messageGenerators.generateErrorMsg("Could not find playlist!")).queue();
                }
                break;

            case "list": // List all playlist by user

                EmbedBuilder msgListBuilder = new EmbedBuilder().setColor(Color.CYAN)
                        .setTitle("All playlists by " + event.getAuthor().getName())
                        .setFooter("Use playlist songs <playlist>** to get the song list.");

                int playlistCounter = 0;
                try {
                    for (PlaylistData playlist : new PlaylistDaoImpl().getPlaylistsByUser(event.getAuthor())) {

                        int songCounter = new PlaylistSongDaoImpl().getSongsByPlaylist(playlist.getId()).size();
                        msgListBuilder.appendDescription(
                                ++playlistCounter + ". **" + playlist.getName() + "** (" + songCounter + " Songs)  \n");
                    }
                    event.getTextChannel().sendMessageEmbeds(msgListBuilder.build()).queue();
                } catch (SQLException e) {
                    event.getTextChannel()
                            .sendMessageEmbeds(super.messageGenerators.generateErrorMsg("Could not find any playlist!"))
                            .queue();
                }
                break;
            case "songs": // List all songs from a playlist
                if (parsedCommand.getArgs().length < 2) {
                    event.getTextChannel().sendMessageEmbeds(super.messageGenerators.generateErrorMsgWrongInput()).queue();
                    return;
                }

                EmbedBuilder songBoardBuilder = new EmbedBuilder().setColor(Color.CYAN)
                        .setTitle("All song from the playlist `" + parsedCommand.getArgsAsList().get(1) + "`")
                        .setFooter("Use playlist play " + parsedCommand.getArgsAsList().get(1)
                                + " to play your playlist.");
                try {
                    playlistId = new PlaylistDaoImpl().getPlaylistByUserAndName(event.getAuthor(),
                            parsedCommand.getArgsAsList().get(1));

                    List<String> songs = new PlaylistSongDaoImpl().getSongsByPlaylist(playlistId);
                    if (songs.isEmpty()) {
                        songBoardBuilder.setDescription(
                                "The list is empty. Add a new song with `playlist add <playlist> <song url>`");
                    } else {
                        songBoardBuilder.setDescription(String.join("\n", songs));
                    }
                    event.getTextChannel().sendMessageEmbeds(songBoardBuilder.build()).queue();
                } catch (SQLException e) {
                    event.getTextChannel()
                            .sendMessageEmbeds(super.messageGenerators.generateErrorMsg("Could not find any playlist!"))
                            .queue();
                }
                break;
            default:
                event.getTextChannel().sendMessageEmbeds(super.messageGenerators.generateErrorMsgWrongInput()).queue();
        }
    }

    private boolean checkForMusic(MessageReceivedEvent event) {

        Music music;
        final Member member = event.getMember();

        if (member == null || member.getVoiceState() == null)
            return true;

        // Is not in voice channel
        if (!member.getVoiceState().inVoiceChannel()) {
            event.getTextChannel()
                    .sendMessageEmbeds(super.messageGenerators.generateErrorMsg("You are not in a voice channel!")).queue();
            return true;
        }

        if (member.getVoiceState().getChannel() == null)
            return true;

        MusicManager musicManager = Main.getMusicManager();
        music = musicManager.getMusicByChannel(member.getVoiceState().getChannel());

        // Check: create new Music
        if (music == null && musicManager.isNotMusicInChannel(event.getMember().getVoiceState().getChannel())) {

            if (musicManager.isMusicOwner(event.getMember())) {
                event.getTextChannel()
                        .sendMessageEmbeds(super.messageGenerators.generateErrorMsg(
                                "You have already created an music player. Please go back to your channel to use it!"))
                        .queue();
                return true;
            }
            // Check if afk channel
            if (event.getGuild().getAfkChannel() != null && event.getMember().getVoiceState().getChannel() != null
                    && event.getMember().getVoiceState().getChannel().getId()
                            .equals(event.getGuild().getAfkChannel().getId())) {
                event.getTextChannel().sendMessageEmbeds(
                        super.messageGenerators.generateErrorMsg("You can not listen to music in an afk channel!"))
                        .queue();
                return true;
            }
            // Check if bot available
            if (!Main.getMusicBotManager().botAvailable(event.getGuild())) {
                event.getTextChannel()
                        .sendMessageEmbeds(super.messageGenerators.generateErrorMsg("There is no music bot available!"))
                        .queue();
                return true;
            }
            musicManager.registerMusicByMember(event.getMember(),
                    new Music(event.getMember(), Main.getMusicBotManager().getUnusedBot(event.getGuild())));
            event.getTextChannel().sendMessageEmbeds(super.messageGenerators.generateInfoMsg(
                    "You have now a music instance in your voice chat! Check out the PWI (http://pwi.progen-bot.de/) to control your music more efficient!"))
                    .queue();
            music = musicManager.getMusicByChannel(event.getMember().getVoiceState().getChannel());
        }

        if (music == null) {
            event.getTextChannel()
                    .sendMessageEmbeds(super.messageGenerators.generateErrorMsg("Execute the command `music start` first."))
                    .queue();
            return true;
        }

        return false;
    }

    final Pattern urlPattern = Pattern.compile("^(https?://|www)[a-zA-Z0-9+&@#/%?=~_|!:,.;-]*");

    private boolean isValidURL(String url) {
        final Matcher matcher = urlPattern.matcher(url);
        return matcher.matches();
    }

    @Override
    public AccessLevel getAccessLevel() {
        return AccessLevel.USER;
    }

}
