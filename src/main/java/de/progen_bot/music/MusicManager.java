package de.progen_bot.music;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.Collection;
import java.util.HashMap;

public class MusicManager {

    private HashMap<Guild, HashMap<Member, Music>> musicByGuildByOwner = new HashMap<>();

    public Music getMusicByOwner(Member owner) {
        if (!musicByGuildByOwner.containsKey(owner.getGuild())) return null;
        if (musicByGuildByOwner.get(owner.getGuild()).isEmpty()) return null;
        return musicByGuildByOwner.get(owner.getGuild()).get(owner);
    }

    public Collection<Music> getMusicsByGuild(Guild guild) {
        return musicByGuildByOwner.get(guild).values();
    }

    public Music getMusicByChannel(VoiceChannel channel) {
        if (!musicByGuildByOwner.containsKey(channel.getGuild())) return null;
        for (Music m : musicByGuildByOwner.get(channel.getGuild()).values()) {
            if (m.getChannel().getId().equals(channel.getId())) {
                return m;
            }
        }
        return null;
    }

    public boolean isMusicInChannel(VoiceChannel channel) {
        if (getMusicByChannel(channel) == null) return false;
        return true;
    }

    public boolean isMusicOwner(Member member) {
        if (getMusicByOwner(member) == null) return false;
        return true;
    }

    public Music registerMusicByMember(Member owner, Music music) {
        if (getMusicByChannel(owner.getVoiceState().getChannel()) != null) throw new TooManyMusicForChannelException();
        if (!musicByGuildByOwner.containsKey(owner.getGuild())) musicByGuildByOwner.put(owner.getGuild(), new HashMap<>());
        musicByGuildByOwner.get(owner.getGuild()).put(owner, music);
        return music;
    }

    public void unregisterMusicByOwner(Member owner) {
        if (!musicByGuildByOwner.containsKey(owner.getGuild())) return;
        musicByGuildByOwner.get(owner.getGuild()).remove(owner);
    }
}
