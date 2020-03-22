package de.progen_bot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import de.progen_bot.core.Main;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class Music {

    //private static final int PLAYLIST_LIMIT = 2000;
    private static final AudioPlayerManager MANAGER = new DefaultAudioPlayerManager();

    private final JDA jda;
    private final Member owner;
    private final Guild guild;
    private final GuildVoiceState guildVoiceState;
    private AudioPlayer player;
    private TrackManager trackManager;

    public Music(Member owner, JDA jda) {

        this.owner = owner;
        this.jda = jda;
        this.guild = owner.getGuild();
        this.guildVoiceState = owner.getVoiceState();

        AudioSourceManagers.registerRemoteSources(MANAGER);
        createPlayer();

        if (guildVoiceState == null)
            return;
        final VoiceChannel voiceChannel = guildVoiceState.getChannel();
        if (voiceChannel == null)
            return;

        guild.getAudioManager().openAudioConnection(guild.getVoiceChannelById(voiceChannel.getId()));
    }

    public void createPlayer() {
        if (guildVoiceState == null)
            return;

        player = MANAGER.createPlayer();
        trackManager = new TrackManager(player, guildVoiceState.getChannel());
        player.addListener(trackManager);
        guild.getAudioManager().setSendingHandler(new PlayerSendHandler(player));
    }

    public Member getOwner() {
        return this.owner;
    }

    public JDA getBot() {
        return jda;
    }

    public VoiceChannel getChannel() {
        // Bot needs time to connect
        if (!this.guild.getAudioManager().isConnected()) return this.guildVoiceState.getChannel();
        return this.guild.getAudioManager().getConnectedChannel();
    }

    public boolean hasPlayer() {
        return player != null;
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

        String input = identifier.trim();
        if (!(input.startsWith("http://") || input.startsWith("https://"))) input = "ytsearch: " + input;

        MANAGER.setFrameBufferDuration(1000);
        MANAGER.loadItemOrdered(this.guild, input, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {
                getManager().queue(track, author);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                getManager().queue(playlist.getTracks().get(0), author);
            }

            @Override
            public void noMatches() {
                // do nothing
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

    public void stop() {
        detach();
    }

    public void detach() {
        trackManager.purgeQueue();
        player.destroy();
        this.guild.getAudioManager().closeAudioConnection();
        Main.getMusicBotManager().setBotUnsed(this.guild, jda);
        Main.getMusicManager().unregisterMusicByOwner(this.owner);
        trackManager = null;
        player = null;
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


