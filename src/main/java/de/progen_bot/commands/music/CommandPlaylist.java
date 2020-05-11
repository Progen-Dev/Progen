package de.progen_bot.commands.music;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.core.Main;
import de.progen_bot.db.dao.playlist.PlaylistDaoImpl;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.music.AudioInfo;
import de.progen_bot.music.Music;
import de.progen_bot.permissions.AccessLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommandPlaylist extends CommandHandler {

    public CommandPlaylist() {
        super("playlist", "playlist save <playlist name>` saves the current queue as a playlist\n `playlist load <playlist name>` loads a previously saves playlist \n`playlist list` lists all your playlists \n`playlist delete <playlist name> deletes the playlist", "Handles Playlists! Playlists are globally available. That means you can use them on every guild with Progen!");
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {

        if (parsedCommand.getArgs().length < 1) {
            event.getTextChannel().sendMessage(super.messageGenerators.generateErrorMsgWrongInput()).queue();
            return;
        }

        Music music;

        switch (parsedCommand.getArgsAsList().get(0)) {

            case "save":
            case "s":

                if (!checkForMusic(event)) return;
                music = Main.getMusicManager().getMusicByChannel(event.getMember().getVoiceState().getChannel());

                if (parsedCommand.getArgs().length < 2) {
                    event.getTextChannel().sendMessage(super.messageGenerators.generateErrorMsgWrongInput()).queue();
                    return;
                }

                List<String> newUris = new ArrayList<>();
                newUris.add(music.getPlayer().getPlayingTrack().getInfo().uri);
                for (AudioInfo info : music.getManager().getQueue()) {
                    newUris.add(info.getTrack().getInfo().uri);
                }

                new PlaylistDaoImpl().savePlaylist(newUris, event.getMember().getUser(), parsedCommand.getArgsAsList().get(1));
                break;

            case "load":
            case "l":

                if (!checkForMusic(event)) return;
                music = Main.getMusicManager().getMusicByChannel(event.getMember().getVoiceState().getChannel());

                if (parsedCommand.getArgs().length < 2) {
                    event.getTextChannel().sendMessage(super.messageGenerators.generateErrorMsgWrongInput()).queue();
                    return;
                }

                List<String> uris;
                try {
                    uris = new PlaylistDaoImpl().getPlaylistsByUser(event.getAuthor()).get(parsedCommand.getArgsAsList().get(1));
                } catch (SQLException | NullPointerException e) {
                    event.getTextChannel().sendMessage(super.messageGenerators.generateErrorMsg("Could not find playlist!")).queue();
                    return;
                }
                music.getManager().purgeQueue();
                for (String uri : uris) {
                    music.loadTrack(uri, event.getMember());
                }
                event.getTextChannel().sendMessage(super.messageGenerators.generateInfoMsg("Playlist is loaded in queue after this song!")).queue();
                break;

            case "delete":

                if (parsedCommand.getArgs().length < 2) {
                    event.getTextChannel().sendMessage(super.messageGenerators.generateErrorMsgWrongInput()).queue();
                    return;
                }

                try {
                    new PlaylistDaoImpl().deletePlaylist(event.getAuthor(), parsedCommand.getArgsAsList().get(1));
                    event.getTextChannel().sendMessage(super.messageGenerators.generateInfoMsg("Successfully removed playlist topt!")).queue();
                } catch (SQLException e) {
                    event.getTextChannel().sendMessage(super.messageGenerators.generateErrorMsg("Could not find playlist!")).queue();
                }
                break;

            case "list":

                EmbedBuilder msgListBuilder = new EmbedBuilder()
                        .setColor(Color.CYAN)
                        .setDescription("All playlists by " + event.getAuthor().getName() + ".");
                try {
                    for (Map.Entry<String, List<String>> playlist : new PlaylistDaoImpl().getPlaylistsByUser(event.getAuthor()).entrySet()) {
                        msgListBuilder.addField(playlist.getValue().size() + " songs", playlist.getKey(), false);
                    }
                    event.getTextChannel().sendMessage(msgListBuilder.build()).queue();
                } catch (SQLException e) {
                    event.getTextChannel().sendMessage(super.messageGenerators.generateErrorMsg("Could not find any playlist!")).queue();
                }
                break;

            default:
                event.getTextChannel().sendMessage(super.messageGenerators.generateErrorMsgWrongInput()).queue();
        }
    }

    private boolean checkForMusic(MessageReceivedEvent event) {

        Music music;
        // Is not in voice channel
        if (!event.getMember().getVoiceState().inVoiceChannel()) {
            event.getTextChannel().sendMessage(super.messageGenerators.generateErrorMsg("You are not in a voice channel!")).queue();
            return false;
        }

        music = Main.getMusicManager().getMusicByChannel(event.getMember().getVoiceState().getChannel());

        if (music == null) {
            event.getTextChannel().sendMessage(super.messageGenerators.generateErrorMsg("Execute the command `music start` fist")).queue();
            return false;
        }

        return true;
    }

    @Override
    public String help() {
        return null;
    }

    @Override
    public AccessLevel getAccessLevel() {
        return AccessLevel.USER;
    }

}
