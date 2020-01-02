package de.mtorials.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import de.progen_bot.commands.music.AudioInfo;
import de.progen_bot.commands.music.Music;
import de.progen_bot.commands.music.TrackManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

import java.util.ArrayList;
import java.util.List;

public class GuildMusic {

    private Music music;
    private Guild guild;

    public GuildMusic(Guild guild) {
        this.guild = guild;
        this.music = new Music();
    }

    @JsonGetter
    public boolean hasPlayer() {
        return music.hasPlayer(guild);
    }

    @JsonIgnore
    public TrackManager getManager() {
        if (hasPlayer()) return music.getManager(guild);
        return null;
    }

    @JsonIgnore
    public AudioPlayer getPlayer() {
        if (hasPlayer()) return music.getPlayer(guild);
        return null;
    }

    public boolean isIdle() {
        return music.isIdle(guild);
    }

    public String getTimestamp(long millis) {
        return music.getTimestamp(millis);
    }

    public List<TrackInformation> getQueue() {
        List<TrackInformation> informations = new ArrayList<>();
        for (AudioInfo info : getManager().getQueue()) {
            informations.add(new TrackInformation(info.getTrack()));
        }
        return informations;
    }

    public TrackInformation getCurrentTrackInformation() {
        return new TrackInformation(this.getPlayer().getPlayingTrack());
    }

    // Setter

    public void loadTrack(String identifier, Member author, Message msg) {
        music.loadTrack(identifier, author, msg);
    }

    public void skip() {
        music.skip(guild);
    }
}
