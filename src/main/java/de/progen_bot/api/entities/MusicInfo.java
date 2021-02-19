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
    public boolean hashPlayer()
    {
        return this.music.hashPlayer();
    }

    public TrackManager getManager()
    {
        if (this.hashPlayer())
            return this.music.getManager();

        return null;
    }
}
