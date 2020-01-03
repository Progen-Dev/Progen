package de.mtorials.entities.json;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import de.progen_bot.music.AudioInfo;
import de.progen_bot.music.Music;
import de.progen_bot.music.TrackManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

import java.util.ArrayList;
import java.util.List;

    public class MusicInfo {

        private Music music;

        public MusicInfo(Music music) {
            this.music = music;
        }

        @JsonGetter
        public boolean hasPlayer() {
            return music.hasPlayer();
        }

        @JsonIgnore
        public TrackManager getManager() {
            if (hasPlayer()) return music.getManager();
            return null;
        }

        @JsonIgnore
        public AudioPlayer getPlayer() {
            if (hasPlayer()) return music.getPlayer();
            return null;
        }

        public String getMusicBotName() {
            return music.getBot().getSelfUser().getName();
        }

        public boolean isIdle() {
            return music.isIdle();
        }

        public String getTimestamp() {
            return music.getTimestamp();
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
            music.loadTrack(identifier, author);
        }

        public void skip() {
            music.skip();
        }
    }

