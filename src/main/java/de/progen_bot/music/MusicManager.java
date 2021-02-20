package de.progen_bot.music;

import java.util.Collection;
import java.util.HashMap;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class MusicManager
{
    private final HashMap<String, HashMap<String, Music>> musicByGuildIDByOwnerId = new HashMap<>();

    public Music getMusicByOwner(Member owner)
    {
        if (!this.musicByGuildIDByOwnerId.containsKey(owner.getGuild().getId()))
            return null;
        if (this.musicByGuildIDByOwnerId.get(owner.getGuild().getId()).isEmpty())
            return null;
        
        return this.musicByGuildIDByOwnerId.get(owner.getGuild().getId()).get(owner.getId());
    }

    public Collection<Music> getMusicsByGuild(Guild guild)
    {
        return this.musicByGuildIDByOwnerId.get(guild.getId()).values();
    }

    public Music getMusicByChannel(VoiceChannel channel)
    {
        if (!this.musicByGuildIDByOwnerId.containsKey(channel.getGuild().getId()))
            return null;
        

        for (Music m : musicByGuildIDByOwnerId.get(channel.getGuild().getId()).values())
        {
            if (m.getChannel().getId().equals(channel.getId()))
                return m;
        }

        return null;
    }

    public boolean isNotMusicInChannel(VoiceChannel channel)
    {
        return getMusicByChannel(channel) == null;
    }

    public boolean isMusicOwner(Member member)
    {
        return getMusicByOwner(member) != null;
    }

    public void registerMusicByMember(Member owner, Music music)
    {
        if (owner == null || owner.getVoiceState() == null || owner.getVoiceState().getChannel() == null)
            return;

        if (getMusicByChannel(owner.getVoiceState().getChannel()) != null) throw new TooMuchMusicForChannelException();
        if (!musicByGuildIDByOwnerId.containsKey(owner.getGuild().getId())) musicByGuildIDByOwnerId.put(owner.getGuild().getId(), new HashMap<>());
        musicByGuildIDByOwnerId.get(owner.getGuild().getId()).put(owner.getId(), music);
    }

    public void unregisterMusicByOwner(Member owner)
    {
        if (!musicByGuildIDByOwnerId.containsKey(owner.getGuild().getId())) return;
        musicByGuildIDByOwnerId.get(owner.getGuild().getId()).remove(owner.getId());
    }
}
