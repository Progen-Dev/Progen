package de.progen_bot.music;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.entities.Member;

public class AudioInfo
{
    private final AudioTrack track;
    private final Member author;

    public AudioInfo(AudioTrack track, Member author)
    {
        this.track = track;
        this.author = author;
    }

    public AudioTrack getTrack()
    {
        return this.track;
    }

    @JsonIgnore
    public Member getAuthor()
    {
        return this.author;
    }
}
