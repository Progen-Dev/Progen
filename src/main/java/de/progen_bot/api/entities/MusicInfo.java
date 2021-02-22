package de.progen_bot.api.entities;

import com.fasterxml.jackson.annotation.JsonGetter;

import de.progen_bot.music.Music;
import de.progen_bot.music.TrackManager;

public class MusicInfo
{
    private final Music music;

    public MusicInfo(Music music)
    {
        this.music = music;
    }

    @JsonGetter
    public boolean hasPlayer()
    {
        return this.music.hasPlayer();
    }

    public TrackManager getManager()
    {
        if (this.hasPlayer())
            return this.music.getManager();

        return null;
    }
}
