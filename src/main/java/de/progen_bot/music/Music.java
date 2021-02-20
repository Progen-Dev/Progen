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
    private static final AudioPlayerManager MANAGER = new DefaultAudioPlayerManager();

    private final String ownerId;
    private final String guildId;

    private JDA jda;
    private AudioPlayer player;
    private TrackManager manager;

    public Music(Member owner, JDA jda) {
        this.ownerId = owner.getId();
        this.guildId = owner.getGuild().getId();
        this.jda = jda;

        AudioSourceManagers.registerRemoteSources(MANAGER);
        this.createPlayer();

        final Guild guild = this.jda.getGuildById(guildId);
        final GuildVoiceState state = owner.getVoiceState();
        if (guild == null || state == null)
            return;

        final VoiceChannel channel = state.getChannel();
        if (channel == null)
            return;

        guild.getAudioManager().openAudioConnection(channel);
    }

    private void createPlayer() {
        final Guild guild = jda.getGuildById(this.guildId);
        if (guild == null)
            return;

        final Member owner = guild.getMemberById(this.ownerId);
        if (owner == null)
            return;

        final GuildVoiceState guildVoiceState = owner.getVoiceState();
        if (guildVoiceState == null)
            return;

        final VoiceChannel voiceChannel = guildVoiceState.getChannel();
        if (voiceChannel == null)
            return;

        this.player = MANAGER.createPlayer();
        this.manager = new TrackManager(player, voiceChannel);
        this.player.addListener(this.manager);
        guild.getAudioManager().setSendingHandler(new PlayerSendHandler(player));
    }

    public Member getOwner() {
        final Guild guild = Main.getJDA().getGuildById(this.guildId);
        if (guild == null)
            throw new IllegalStateException("Guild is null");

        final Member owner = guild.getMemberById(this.ownerId);
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
        return this.player != null;
    }

    public AudioPlayer getPlayer() {
        return this.player;
    }

    public TrackManager getManager() {
        return this.manager;
    }

    public boolean isIdle() {
        return !this.hasPlayer() || this.getPlayer().getPlayingTrack() == null;
    }

    public void loadTrack(String identifier, Member author) {
        // Member#getJDA() of author? or author
        Member authorById = getOwner().getGuild().getMemberById(author.getId());
        if (authorById == null)
            return;

        String input = identifier.trim();
        if (!(input.startsWith("http://") || input.startsWith("https://")))
            input = "ytsearch: " + input;

        MANAGER.setFrameBufferDuration(1000);
        MANAGER.loadItemOrdered(jda.getGuildById(this.guildId), input, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track)
            {
                getManager().queue(track, authorById);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist)
            {
                getManager().queue(playlist.getTracks().get(0), authorById);
            }

            @Override
            public void noMatches()
            {
                System.out.println("no match for that url.");
            }

            @Override
            public void loadFailed(FriendlyException e)
            {
                // TODO better handling
                e.printStackTrace();
            }
        });
    }

    public void skip()
    {
        this.getPlayer().stopTrack();
    }

    public void stop()
    {
        this.detach();
    }

    public void detach()
    {
        this.manager.purgeQueue();
        this.player.destroy();
        // Guild can be null here, member too
        this.getOwner().getGuild().getAudioManager().closeAudioConnection();
        Main.getMusicBotManager().setBotUnused(getOwner().getGuild().getIdLong(), jda);
        Main.getMusicManager().unregisterMusicByOwner(getOwner());
        this.manager = null;
        jda = null;
        player = null;
    }

    // TODO
    public String getTimestamp()
    {

        long seconds = getPlayer().getPlayingTrack().getPosition() / 1000;

        return /* CommandMusic.getTimestampBySeconds(seconds) */ "";
    }
}
