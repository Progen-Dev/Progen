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
import de.progen_bot.db.entities.config.GuildConfiguration;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class Music extends CommandHandler {
    public Music() {
        super("music", "music play <name or link>, music stop, music skip, music resume ", "games music of all kind with progen in your voice channel");
        AudioSourceManagers.registerRemoteSources(MANAGER);
    }


    private static final int PLAYLIST_LIMIT = 2000;
    private static Guild guild;
    private static final AudioPlayerManager MANAGER = new DefaultAudioPlayerManager();
    private static final Map<Guild, Map.Entry<AudioPlayer, TrackManager>> PLAYERS = new HashMap<>();

    /**
     * @param g Guild
     * @return AudioPlayer
     */

    public AudioPlayer createPlayer(Guild g) {
        AudioPlayer p = MANAGER.createPlayer();
        TrackManager m = new TrackManager(p);
        p.addListener(m);
        guild.getAudioManager().setSendingHandler(new PlayerSendHandler(p));
        PLAYERS.put(g, new AbstractMap.SimpleEntry<>(p, m));
        return p;
    }

    /**
     * @param g Guild
     * @return Boolean
     */

    public boolean hasPlayer(Guild g) {
        return PLAYERS.containsKey(g);
    }

    /**
     * oder erstellt einen neuen Player für die Guild.
     *
     * @param g Guild
     * @return AudioPlayer
     */

    public AudioPlayer getPlayer(Guild g) {
        if (hasPlayer(g))
            return PLAYERS.get(g).getKey();
        else
            return createPlayer(g);
    }


    /**
     * @param g Guild
     * @return TrackManager
     */
    public TrackManager getManager(Guild g) {
        return PLAYERS.get(g).getValue();
    }

    /**
     * gerade einen Track spielt.
     *
     * @param g Guild
     * @return Boolean
     */

    public boolean isIdle(Guild g) {
        return !hasPlayer(g) || getPlayer(g).getPlayingTrack() == null;
    }

    /**
     * @param identifier URL oder Search String
     * @param author     Member, der den Track / die Playlist eingereiht hat
     * @param msg        Message des Contents
     */

    public void loadTrack(String identifier, Member author, Message msg) {
        Guild guild = author.getGuild();
        getPlayer(guild);
        MANAGER.setFrameBufferDuration(1000);
        MANAGER.loadItemOrdered(guild, identifier, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {
                getManager(guild).queue(track, author);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                for (int i = 0; i < (playlist.getTracks().size() > PLAYLIST_LIMIT ? PLAYLIST_LIMIT : playlist.getTracks().size()); i++) {
                    getManager(guild).queue(playlist.getTracks().get(i), author);
                }
            }

            @Override
            public void noMatches() {
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                exception.printStackTrace();
            }
        });
    }

    /**
     * @param g Guild
     */
    public void skip(Guild g) {
        getPlayer(g).stopTrack();
    }

    /**
     * @param milis Timestamp
     * @return Zeitformat
     */

    public String getTimestamp(long milis) {

        long seconds = milis / 1000;

        long hours = Math.floorDiv(seconds, 3600);
        seconds = seconds - (hours * 3600);
        long mins = Math.floorDiv(seconds, 60);
        seconds = seconds - (mins * 60);
        return (hours == 0 ? "" : hours + ":") + String.format("%02d", mins) + ":" + String.format("%02d", seconds);
    }

    /**
     * Returnt aus der AudioInfo eines Tracks die Informationen als String.
     *
     * @param info AudioInfo
     * @return Informationen als String
     */

    private String buildQueueMessage(AudioInfo info) {
        AudioTrackInfo trackInfo = info.getTrack().getInfo();
        String title = trackInfo.title;
        long length = trackInfo.length;
        return "`[ " + getTimestamp(length) + " ]` " + title + "\n";
    }

    /**
     * Sendet eine Embed-Message in der Farbe Rot mit eingegebenen Content.
     *
     * @param event   MessageReceivedEvent
     * @param content Error Message Content
     */

    private void sendErrorMsg(MessageReceivedEvent event, String content) {
        event.getTextChannel().sendMessage(
                new EmbedBuilder()
                        .setColor(Color.red)
                        .setDescription(content)
                        .build()
        ).queue();
    }


    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {
        guild = event.getGuild();
        if (parsedCommand.getArgs().length < 1) {
            sendErrorMsg(event, help());
            return;
        }
        switch (parsedCommand.getArgs()[0].toLowerCase()) {
            case "play":
            case "p":
                if (parsedCommand.getArgs().length < 2) {

                    sendErrorMsg(event, "Please enter a valid source!");

                    return;
                }

                String input = Arrays.stream(parsedCommand.getArgs()).skip(1).map(s -> " " + s).collect(Collectors.joining()).substring(1);

                if (!(input.startsWith("http://") || input.startsWith("https://")))
                    input = "ytsearch: " + input;
                loadTrack(input, event.getMember(), event.getMessage());
                break;
            case "skip":
            case "s":
                if (isIdle(guild)) return;
                for (int i = (parsedCommand.getArgs().length > 1 ? Integer.parseInt(parsedCommand.getArgs()[1]) : 1); i == 1; i--) {
                    skip(guild);
                }
                break;
            case "stop":
                if (isIdle(guild)) return;
                getManager(guild).purgeQueue();
                skip(guild);
                guild.getAudioManager().closeAudioConnection();


                break;
            case "shuffle":
                if (isIdle(guild)) return;
                getManager(guild).shuffleQueue();
                break;
            case "now":
            case "info":

                if (isIdle(guild)) return;
                AudioTrack track = getPlayer(guild).getPlayingTrack();
                AudioTrackInfo info = track.getInfo();
                event.getTextChannel().sendMessage(
                        new EmbedBuilder()
                                .setDescription("**CURRENT TRACK INFO:**")
                                .addField("Title", info.title, false)
                                .addField("Duration", "`[ " + getTimestamp(track.getPosition()) + "/ " + getTimestamp(track.getDuration()) + " ]`", false)
                                .addField("Author", ((AudioTrackInfo) info).author, false)
                                .build()
                ).queue();
                break;
            case "queue":
                if (isIdle(guild)) return;
                int sideNumb = parsedCommand.getArgs().length > 1 ? Integer.parseInt(parsedCommand.getArgs()[1]) : 1;
                List<String> tracks = new ArrayList<>();
                List<String> trackSublist;
                getManager(guild).getQueue().forEach(audioInfo -> tracks.add(buildQueueMessage(audioInfo)));
                if (tracks.size() > 20)
                    trackSublist = tracks.subList((sideNumb - 1) * 20, (sideNumb - 1) * 20 + 20);
                else
                    trackSublist = tracks;
                String out = trackSublist.stream().collect(Collectors.joining("\n"));
                int sideNumbAll = tracks.size() >= 20 ? tracks.size() / 20 : 1;
                event.getTextChannel().sendMessage(
                        new EmbedBuilder()
                                .setDescription(
                                        "**CURRENT QUEUE:**\n" +
                                                "*[" + getManager(guild).getQueue().stream() + " Tracks | Side " + sideNumb + " / " + sideNumbAll + "]*" +
                                                out
                                )
                                .build()
                ).queue();
                break;
        }
    }


    @Override
    public String help() {
        return null;
    }

}


