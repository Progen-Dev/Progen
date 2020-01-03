package de.progen_bot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
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

public class Music {

    private static final int PLAYLIST_LIMIT = 2000;
    private static final AudioPlayerManager MANAGER = new DefaultAudioPlayerManager();

    private Member owner;
    private AudioPlayer player;
    private TrackManager manager;

    public Music(Member owner) {

        this.owner = owner;
        createPlayer();
    }

    public void createPlayer() {
        player = MANAGER.createPlayer();
        manager = new TrackManager(player);
        player.addListener(manager);
        owner.getGuild().getAudioManager().setSendingHandler(new PlayerSendHandler(player));
    }

    public boolean hasPlayer() {
        if (player != null) return true;
        return false;
    }

    public AudioPlayer getPlayer() {
        return player;
    }

    public TrackManager getManager() {
        return manager;
    }

    public boolean isIdle() {
        return !hasPlayer() || getPlayer().getPlayingTrack() == null;
    }

    public void loadTrack(String identifier, Member author) {
        Guild guild = owner.getGuild();
        MANAGER.setFrameBufferDuration(1000);
        MANAGER.loadItemOrdered(guild, identifier, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {
                getManager().queue(track, author);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                for (int i = 0; i < (playlist.getTracks().size() > PLAYLIST_LIMIT ? PLAYLIST_LIMIT : playlist.getTracks().size()); i++) {
                    getManager().queue(playlist.getTracks().get(i), author);
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

    public void skip() {
        getPlayer().stopTrack();
    }

    public String getTimestamp() {

        long seconds = getPlayer().getPlayingTrack().getPosition() / 1000;

        long hours = Math.floorDiv(seconds, 3600);
        seconds = seconds - (hours * 3600);
        long mins = Math.floorDiv(seconds, 60);
        seconds = seconds - (mins * 60);
        return (hours == 0 ? "" : hours + ":") + String.format("%02d", mins) + ":" + String.format("%02d", seconds);
    }
}


