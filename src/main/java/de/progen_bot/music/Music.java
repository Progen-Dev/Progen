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
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class Music {

    //private static final int PLAYLIST_LIMIT = 2000;
    private static final AudioPlayerManager MANAGER = new DefaultAudioPlayerManager();

    private String ownerID;
    private String guildID;

    private JDA jda;
    private AudioPlayer player;
    private TrackManager trackManager;

    public Music(Member owner, JDA jda) {

        this.ownerID = owner.getId();
        this.jda = jda;

        this.guildID = owner.getGuild().getId();

        AudioSourceManagers.registerRemoteSources(MANAGER);
        createPlayer();
        // Guild, member, guildvoicestate and voicestate channel can be null
        jda.getGuildById(guildID).getAudioManager().openAudioConnection(jda.getGuildById(guildID).getVoiceChannelById(owner.getVoiceState().getChannel().getId()));
    }

    public void createPlayer() {
        // Guild, member, guildvoicestate and voicestate channel can be null
        player = MANAGER.createPlayer();
        trackManager = new TrackManager(player, jda.getGuildById(guildID).getMemberById(ownerID).getVoiceState().getChannel());
        player.addListener(trackManager);
        jda.getGuildById(guildID).getAudioManager().setSendingHandler(new PlayerSendHandler(player));
    }

    public Member getOwner() {
        // Guild and member can be null
        return Main.getJda().getGuildById(guildID).getMemberById(ownerID);
    }

    public JDA getBot() {
        return jda;
    }

    public VoiceChannel getChannel() {
        // Bot needs time to connect
        // Guild can be null
        if (!getBot().getGuildById(getOwner().getGuild().getId()).getAudioManager().isConnected()) return jda.getGuildById(guildID).getMemberById(ownerID).getVoiceState().getChannel();
        return getBot().getGuildById(getOwner().getGuild().getId()).getAudioManager().getConnectedChannel();
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
        // Member#getJDA() of author? or author
        Member auhtorInJDA = jda.getGuildById(author.getGuild().getId()).getMemberById(author.getId());

        String input = identifier.trim();
        if (!(input.startsWith("http://") || input.startsWith("https://"))) input = "ytsearch: " + input;

        MANAGER.setFrameBufferDuration(1000);
        MANAGER.loadItemOrdered(jda.getGuildById(guildID), input, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {
                getManager().queue(track, auhtorInJDA);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                getManager().queue(playlist.getTracks().get(0), auhtorInJDA);
            }

            @Override
            public void noMatches() {
                // nothing
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
        // Guild can be null here, member too
        jda.getGuildById(guildID).getAudioManager().closeAudioConnection();
        Main.getMusicBotManager().setBotUnsed(Main.getJda().getGuildById(guildID), jda);
        Main.getMusicManager().unregisterMusicByOwner(Main.getJda().getGuildById(guildID).getMemberById(ownerID));
        trackManager = null;
        jda = null;
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
