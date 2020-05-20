package de.progen_bot.music;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.Collection;
import java.util.HashMap;

public class MusicManager {

    private final HashMap<String, HashMap<String, Music>> musicByGuildIDByOwnerID = new HashMap<>();

    public Music getMusicByOwner(Member owner) {
        if (!musicByGuildIDByOwnerID.containsKey(owner.getGuild().getId())) return null;
        if (musicByGuildIDByOwnerID.get(owner.getGuild().getId()).isEmpty()) return null;
        return musicByGuildIDByOwnerID.get(owner.getGuild().getId()).get(owner.getId());
    }

    public Collection<Music> getMusicsByGuild(Guild guild) {
        return musicByGuildIDByOwnerID.get(guild.getId()).values();
    }

    public Music getMusicByChannel(VoiceChannel channel) {
        if (!musicByGuildIDByOwnerID.containsKey(channel.getGuild().getId())) return null;
        for (Music m : musicByGuildIDByOwnerID.get(channel.getGuild().getId()).values()) {
            if (m.getChannel().getId().equals(channel.getId())) {
                return m;
            }
        }
        return null;
    }

    public boolean isMusicInChannel(VoiceChannel channel) {
        return getMusicByChannel(channel) != null;
    }

    public boolean isMusicOwner(Member member) {
        return getMusicByOwner(member) != null;
    }

    // I would suggest, that this method will be void
    public Music registerMusicByMember(Member owner, Music music) {
        if (owner == null || owner.getVoiceState() == null || owner.getVoiceState().getChannel() == null)
            return null;

        if (getMusicByChannel(owner.getVoiceState().getChannel()) != null) throw new TooManyMusicForChannelException();
        if (!musicByGuildIDByOwnerID.containsKey(owner.getGuild().getId())) musicByGuildIDByOwnerID.put(owner.getGuild().getId(), new HashMap<>());
        musicByGuildIDByOwnerID.get(owner.getGuild().getId()).put(owner.getId(), music);
        return music;
    }

    public void unregisterMusicByOwner(Member owner) {
        if (!musicByGuildIDByOwnerID.containsKey(owner.getGuild().getId())) return;
        musicByGuildIDByOwnerID.get(owner.getGuild().getId()).remove(owner.getId());
    }
}
