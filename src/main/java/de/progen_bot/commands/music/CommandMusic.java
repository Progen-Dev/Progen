package de.progen_bot.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.core.Main;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.music.AudioInfo;
import de.progen_bot.music.Music;
import de.progen_bot.music.PlayerSendHandler;
import de.progen_bot.music.TrackManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class CommandMusic extends CommandHandler {

    public CommandMusic() {
        super("music", "music play <name or link>, music stop, music skip, music resume ", "games music of all kind with progen in your voice channel");
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {

        if (parsedCommand.getArgs().length < 1) {
            event.getTextChannel().sendMessage(super.messageGenerators.generateErrorMsgWrongInput()).queue();
            return;
        }

        Music music;
        // Is not in voice channel
        if (!event.getMember().getVoiceState().inVoiceChannel()) {
            event.getTextChannel().sendMessage(super.messageGenerators.generateErrorMsg("You are not in a voice channel!")).queue();
            return;
        }

        // Check: create new Music
        if (parsedCommand.getArgsAsList().get(0).equals("play") || parsedCommand.getArgsAsList().get(0).equals("p")) {
            // If no Music in Channel
            if (Main.getMusicManager().getMusicByChannel(event.getMember().getVoiceState().getChannel()) == null) {

                if (Main.getMusicManager().getMusicByOwner(event.getMember()) != null) {
                    event.getTextChannel().sendMessage(super.messageGenerators.generateErrorMsg("You have already created an music player. Please go back to your channel to use it!")).queue();
                    return;
                }
                //Check if afk channel
                if (event.getMember().getVoiceState().getChannel().getId().equals(event.getGuild().getAfkChannel().getId())) {

                    event.getTextChannel().sendMessage(super.messageGenerators.generateErrorMsg("You can not listen to music in an afk channel!")).queue();
                    return;
                }
                Main.getMusicManager().registerMusicByMember(event.getMember(), new Music(event.getMember(), Main.getMusicBotManager().getUnusedBotForMember(event.getGuild())));
            }
        }

        music = Main.getMusicManager().getMusicByChannel(event.getMember().getVoiceState().getChannel());

        if (music == null) {
            event.getTextChannel().sendMessage(super.messageGenerators.generateErrorMsg("Execute the command `music play` fist")).queue();
            return;
        }

        switch (parsedCommand.getArgs()[0].toLowerCase()) {
            case "play":
            case "p":

                if (parsedCommand.getArgs().length < 2) {
                    event.getTextChannel().sendMessage(super.messageGenerators.generateErrorMsg( "Please enter a valid source!")).queue();
                    music.stop();
                    return;
                }

                music.loadTrack(Arrays.stream(parsedCommand.getArgs()).skip(1).map(s -> " " + s).collect(Collectors.joining()).substring(1), event.getMember());
                break;

            case "stop":
                music.stop();
                break;

            case "skip":
                music.skip();
                break;

            case "queue":
            case "q":
                EmbedBuilder msgQueueBuilder = new EmbedBuilder()
                        .setTitle("Queue")
                        .setColor(Color.CYAN)
                        .setDescription("The cue of your music player bot!");

                for (AudioInfo track :  music.getManager().getQueue()) {
                    msgQueueBuilder.addField(track.getTrack().getInfo().title, "`" + getTimestamp(track.getTrack().getInfo().length) + "`", false);
                }

                event.getTextChannel().sendMessage(msgQueueBuilder.build()).queue();
                break;

            case "info":
            case "i":

                EmbedBuilder msgInfoBuilder = new EmbedBuilder()
                        .setTitle("Track Info")
                        .setColor(Color.CYAN)
                        .setDescription("The current track");

                msgInfoBuilder.addField("Title", music.getPlayer().getPlayingTrack().getInfo().title, false);
                msgInfoBuilder.addField("Interpret / Uploader", music.getPlayer().getPlayingTrack().getInfo().author, false);
                msgInfoBuilder.addField("Position", "`" + getTimestamp(music.getPlayer().getPlayingTrack().getPosition()) + "/" + getTimestamp(music.getPlayer().getPlayingTrack().getInfo().length) + "`", false);
                msgInfoBuilder.addField("URI", music.getPlayer().getPlayingTrack().getInfo().uri, false);

                event.getTextChannel().sendMessage(msgInfoBuilder.build()).queue();
                break;

            default:
                event.getTextChannel().sendMessage(super.messageGenerators.generateErrorMsgWrongInput()).queue();
        }
    }

    private String getTimestamp(long millis) {

        long seconds = millis / 1000;

        long hours = Math.floorDiv(seconds, 3600);
        seconds = seconds - (hours * 3600);
        long mins = Math.floorDiv(seconds, 60);
        seconds = seconds - (mins * 60);
        return (hours == 0 ? "" : hours + ":") + String.format("%02d", mins) + ":" + String.format("%02d", seconds);
    }

    @Override
    public String help() {
        return null;
    }

}


