package de.mtorials.entities;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class TrackInformation {

    AudioTrack track;

    TrackInformation(AudioTrack track) {
        this.track = track;
    }

    public String getTitle() {
        return track.getInfo().title;
    }

    public String getAuthor() {
        return track.getInfo().author;
    }

    public String getURI() {
        return track.getInfo().uri;
    }

    public long getLength() {
        return track.getDuration();
    }

    public long getPosition() {
        return track.getPosition();
    }
}
