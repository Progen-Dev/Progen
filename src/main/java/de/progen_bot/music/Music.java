package de.progen_bot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

public class Music {

    private static final int PLAYLIST_LIMIT = 2000;
    private static final AudioPlayerManager MANAGER = new DefaultAudioPlayerManager();

    private Member owner;
    private JDA jda;
    private AudioPlayer player;
    private TrackManager trackManager;

    public Music(Member owner, JDA jda) {

        this.owner = owner;
        this.jda = jda;
        AudioSourceManagers.registerRemoteSources(MANAGER);
        createPlayer();
    }

    public void createPlayer() {
        player = MANAGER.createPlayer();
        trackManager = new TrackManager(player, jda.getVoiceChannelById(owner.getVoiceState().getChannel().getId()));
        player.addListener(trackManager);
        jda.getGuildById(owner.getGuild().getId()).getAudioManager().setSendingHandler(new PlayerSendHandler(player));
    }

    public Member getOwner() {
        return owner;
    }

    public boolean hasPlayer() {
        if (player != null) return true;
        return false;
    }

    public AudioPlayer getPlayer() {
        return player;
    }

    public TrackManager getManager() {
        return trackManager;
    }

    public boolean isIdle() {
        return !hasPlayer() || getPlayer().getPlayingTrack() == null;
    }

    public void loadTrack(String identifier, Member author) {
        Guild guild = jda.getGuildById(owner.getGuild().getId());
        Member auhtorInJDA = jda.getGuildById(author.getGuild().getId()).getMemberById(author.getId());
        MANAGER.setFrameBufferDuration(1000);
        MANAGER.loadItemOrdered(guild, identifier, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {
                getManager().queue(track, auhtorInJDA);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                for (int i = 0; i < (playlist.getTracks().size() > PLAYLIST_LIMIT ? PLAYLIST_LIMIT : playlist.getTracks().size()); i++) {
                    getManager().queue(playlist.getTracks().get(i), auhtorInJDA);
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


