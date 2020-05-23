package de.progen_bot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import de.progen_bot.commands.music.CommandMusic;
import de.progen_bot.core.Main;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class Music {

    //private static final int PLAYLIST_LIMIT = 2000;
    private static final AudioPlayerManager MANAGER = new DefaultAudioPlayerManager();

    private final String ownerID;
    private final String guildID;

    private JDA jda;
    private AudioPlayer player;
    private TrackManager trackManager;

    public Music(Member owner, JDA jda) {

        this.ownerID = owner.getId();
        this.jda = jda;

        this.guildID = owner.getGuild().getId();

        AudioSourceManagers.registerRemoteSources(MANAGER);
        createPlayer();

        final Guild guild = jda.getGuildById(guildID);
        final GuildVoiceState guildVoiceState = owner.getVoiceState();
        if (guild == null || guildVoiceState == null)
            return;

        final VoiceChannel channel = guildVoiceState.getChannel();
        if (channel == null)
            return;

        final VoiceChannel vc = guild.getVoiceChannelById(guildVoiceState.getChannel().getId());
        guild.getAudioManager().openAudioConnection(vc);
    }

    public void createPlayer() {
        // Guild, member, guildvoicestate and voicestate channel can be null
        final Guild guild = jda.getGuildById(guildID);
        if (guild == null)
            return;

        final Member owner = guild.getMemberById(ownerID);
        if (owner == null)
            return;

        final GuildVoiceState guildVoiceState = owner.getVoiceState();
        if (guildVoiceState == null)
            return;

        final VoiceChannel voiceChannel = guildVoiceState.getChannel();
        if (voiceChannel == null)
            return;

        player = MANAGER.createPlayer();
        trackManager = new TrackManager(player, voiceChannel);
        player.addListener(trackManager);
        guild.getAudioManager().setSendingHandler(new PlayerSendHandler(player));
    }

    public Member getOwner() {
        final Guild guild = Main.getJda().getGuildById(guildID);
        if (guild == null)
            throw new IllegalStateException("Guild is null");

        final Member owner = guild.getMemberById(ownerID);
        if (owner == null)
            throw new IllegalStateException("Owner is null");

        return owner;
    }

    public JDA getBot() {
        return jda;
    }

    public VoiceChannel getChannel() {
        // Bot needs time to connect
        /* Null check already */
        final Member owner = getOwner();
        final Guild guild = owner.getGuild();
        final GuildVoiceState guildVoiceState = owner.getVoiceState();

        if (!guild.getAudioManager().isConnected() && guildVoiceState != null && guildVoiceState.getChannel() != null) {
            return guildVoiceState.getChannel();
        }

        return guild.getAudioManager().getConnectedChannel();
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
        Member authorById = getOwner().getGuild().getMemberById(author.getId());
        if (authorById == null)
            return;

        String input = identifier.trim();
        if (!(input.startsWith("http://") || input.startsWith("https://"))) input = "ytsearch: " + input;

        MANAGER.setFrameBufferDuration(1000);
        MANAGER.loadItemOrdered(jda.getGuildById(guildID), input, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {
                getManager().queue(track, authorById);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                getManager().queue(playlist.getTracks().get(0), authorById);
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
        getOwner().getGuild().getAudioManager().closeAudioConnection();
        Main.getMusicBotManager().setBotUnused(getOwner().getGuild(), jda);
        Main.getMusicManager().unregisterMusicByOwner(getOwner());
        trackManager = null;
        jda = null;
        player = null;
    }

    public String getTimestamp() {

        long seconds = getPlayer().getPlayingTrack().getPosition() / 1000;

        return CommandMusic.getTimestampBySeconds(seconds);
    }
}
