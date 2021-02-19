package de.progen_bot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class Music
{
    private static final AudioPlayerManager MANAGER = new DefaultAudioPlayerManager();

    private final String ownerId;
    private final String guildId;

    private JDA jda;
    private AudioPlayer player;
    private TrackManager manager;

    public Music(Member owner, JDA jda)
    {
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

	private void createPlayer()
    {
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

    public boolean hasPlayer() {
		return false;
	}

	public TrackManager getManager() {
		return null;
	}
    
}
