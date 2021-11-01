package de.progen_bot.commands.music;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.core.Main;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.music.*;
import de.progen_bot.permissions.AccessLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.*;
import java.util.stream.Collectors;

public class CommandMusic extends CommandHandler {

    public CommandMusic() {
        super("music",
                "`music play or p <name oder URL>` adds music to queue\n`music stop` stops the music\n `music skip or s` skips a song\n `music playpause or pp` pauses the music\n `music queue` see the current queue \n`music info or i` see information about the current song",
                "Plays music of all kind with Progen in your voice channel");
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event,
            GuildConfiguration configuration) {

        if (event.getMember() == null || event.getMember().getVoiceState() == null)
            return;

        MusicManager musicManager = Main.getMusicManager();

        if (parsedCommand.getArgs().length < 1) {
            event.getTextChannel().sendMessageEmbeds(super.messageGenerators.generateErrorMsgWrongInput()).queue();
            return;
        }

        Music music;
        // Is not in voice channel
        if (!event.getMember().getVoiceState().inVoiceChannel()) {
            event.getTextChannel()
                    .sendMessageEmbeds(super.messageGenerators.generateErrorMsg("You are not in a voice channel!")).queue();
            return;
        }

        if (event.getMember().getVoiceState().getChannel() == null)
            return;

        music = musicManager.getMusicByChannel(event.getMember().getVoiceState().getChannel());

        // Check: create new Music
        if (music == null && musicManager.isNotMusicInChannel(event.getMember().getVoiceState().getChannel())) {

            if (musicManager.isMusicOwner(event.getMember())) {
                event.getTextChannel()
                        .sendMessageEmbeds(super.messageGenerators.generateErrorMsg(
                                "You have already created an music player. Please go back to your channel to use it!"))
                        .queue();
                return;
            }
            // Check if afk channel
            if (event.getGuild().getAfkChannel() != null && event.getMember().getVoiceState().getChannel() != null
                    && event.getMember().getVoiceState().getChannel().getId()
                            .equals(event.getGuild().getAfkChannel().getId())) {
                event.getTextChannel().sendMessageEmbeds(
                        super.messageGenerators.generateErrorMsg("You can not listen to music in an afk channel!"))
                        .queue();
                return;
            }
            // Check if bot available
            if (!Main.getMusicBotManager().botAvailable(event.getGuild())) {
                event.getTextChannel()
                        .sendMessageEmbeds(super.messageGenerators.generateErrorMsg("There is no music bot available!"))
                        .queue();
                return;
            }
            musicManager.registerMusicByMember(event.getMember(),
                    new Music(event.getMember(), Main.getMusicBotManager().getUnusedBot(event.getGuild())));
            event.getTextChannel().sendMessageEmbeds(super.messageGenerators.generateInfoMsg(
                    "You have now a music instance in your voice chat! Check out the PWI (http://pwi.progen-bot.de/) to control your music more efficient!"))
                    .queue();
            music = musicManager.getMusicByChannel(event.getMember().getVoiceState().getChannel());
        }

        if (music == null)
            return;

        switch (parsedCommand.getArgs()[0].toLowerCase()) {
            case "play":
            case "p":

                if (parsedCommand.getArgs().length < 2) {
                    event.getTextChannel()
                            .sendMessageEmbeds(super.messageGenerators.generateErrorMsg("Please enter a valid source!"))
                            .queue();
                    return;
                }

                music.loadTrack(Arrays.stream(parsedCommand.getArgs()).skip(1).map(s -> " " + s)
                        .collect(Collectors.joining()).substring(1), event.getMember());
                break;

            case "stop":
                music.stop();
                break;

            case "skip":
            case "s":
                music.skip();
                break;

            case "playpause":
            case "pp":
                music.getPlayer().setPaused(!music.getPlayer().isPaused());
                break;

            case "queue":
            case "q":
                EmbedBuilder msgQueueBuilder = new EmbedBuilder().setTitle("Queue").setColor(Color.CYAN)
                        .setDescription("The queue of your music player bot!");

                if (music.getManager().getQueue().isEmpty()) {
                    msgQueueBuilder.setDescription("The queue is empty.");
                } else {
                    for (AudioInfo track : music.getManager().getQueue()) {
                        msgQueueBuilder.addField(track.getTrack().getInfo().title,
                                "`" + getTimestamp(track.getTrack().getInfo().length) + "`", false);
                    }
                }

                event.getTextChannel().sendMessageEmbeds(msgQueueBuilder.build()).queue();
                break;

            case "info":
            case "i":

                EmbedBuilder msgInfoBuilder = new EmbedBuilder().setTitle("Track Info").setColor(Color.CYAN)
                        .setDescription("The current track");

                msgInfoBuilder.addField("Title", music.getPlayer().getPlayingTrack().getInfo().title, false);
                msgInfoBuilder.addField("Interpret / Uploader", music.getPlayer().getPlayingTrack().getInfo().author,
                        false);
                msgInfoBuilder
                        .addField("Position",
                                "`" + getTimestamp(music.getPlayer().getPlayingTrack().getPosition()) + "/"
                                        + getTimestamp(music.getPlayer().getPlayingTrack().getInfo().length) + "`",
                                false);
                msgInfoBuilder.addField("URI", music.getPlayer().getPlayingTrack().getInfo().uri, false);

                event.getTextChannel().sendMessageEmbeds(msgInfoBuilder.build()).queue();
                break;

            default:
                event.getTextChannel().sendMessageEmbeds(super.messageGenerators.generateErrorMsgWrongInput()).queue();
        }
    }

    private String getTimestamp(long millis) {

        long seconds = millis / 1000;

        return getTimestampBySeconds(seconds);
    }

    @NotNull
    public static String getTimestampBySeconds(long seconds) {
        long hours = Math.floorDiv(seconds, 3600);
        seconds = seconds - (hours * 3600);
        long mins = Math.floorDiv(seconds, 60);
        seconds = seconds - (mins * 60);
        return (hours == 0 ? "" : hours + ":") + String.format("%02d", mins) + ":" + String.format("%02d", seconds);
    }

    @Override
    public AccessLevel getAccessLevel() {
        return AccessLevel.USER;
    }

}
