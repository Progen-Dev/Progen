package de.progen_bot.api.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class TrackInformation
{
    private final AudioTrack track;

    public TrackInformation(AudioTrack track)
    {
        this.track = track;
    }

    public String getTitle()
    {
        return this.track.getInfo().title;
    }

    public String getAuthor()
    {
        return this.track.getInfo().author;
    }

    public String getURI()
    {
        return this.track.getInfo().uri;
    }

    public long getLength()
    {
        return this.track.getDuration();
    }

    public long getPosition()
    {
        return this.track.getPosition();
    }

    @JsonIgnore
    public AudioTrack getAudioTrack()
    {
        return this.track;
    }
}
