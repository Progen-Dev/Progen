package de.progen_bot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackManager extends AudioEventAdapter {
    private final AudioPlayer player;
    private final Queue<AudioInfo> queue;
    private final VoiceChannel voiceChannel;

    /**
     * Creates an instance of the TrackManager class.
     * @param player {@link AudioPlayer audio player}
     *
     */

    public TrackManager(AudioPlayer player, VoiceChannel voiceChannel) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
        this.voiceChannel = voiceChannel;
        //this.music = music;
    }

    /**
     *Lines the transferred track into the queue.
     *
     * @param track  AudioTrack
     * @param author Member who was queue the track
     */

    public void queue(AudioTrack track, Member author) {
        AudioInfo info = new AudioInfo(track, author);
        queue.add(info);
        if (player.getPlayingTrack() == null) {
            player.playTrack(queue.element().getTrack());
            queue.remove();
        }
    }

    /**
     * Returns the current queue as LinkedHashSet.
     *
     * @return Queue
     */

    public Set<AudioInfo> getQueue() {
        return new LinkedHashSet<>(queue);
    }

    /**
     * Returns audioinfo of the Track from the Queue.
     *
     * @param track AudioTrack
     * @return AudioInfo
     */

    public AudioInfo getInfo(AudioTrack track) {
        return queue.stream()
                .filter(info -> info.getTrack().equals(track))
                .findFirst().orElse(null);

    }

    /**
     * Empties the entire Queue
     */

    public void purgeQueue() {

        queue.clear();
    }


    /**
     * Shufflet the current Queue.
     */

    public void shuffleQueue() {
        List<AudioInfo> cQueue = new ArrayList<>(getQueue());
        AudioInfo current = cQueue.get(0);
        cQueue.remove(0);
        Collections.shuffle(cQueue);
        cQueue.add(0, current);
        purgeQueue();
        queue.addAll(cQueue);
    }

    /**
     * PLAYER EVENT: TRACK LAUNCHES
     * If single row is not in the voice channel, the player will stop.
     * Otherwise the bot will connect to the voice channel of the Track Owner.
     * @param player AudioPlayer
     * @param track  AudioTrack
     */

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        VoiceChannel vChan = voiceChannel;
        if (vChan == null)
            player.stopTrack();
        else
            voiceChannel.getGuild().getAudioManager().openAudioConnection(vChan);
    }

    /**
     * PLAYER EVENT: TRACK ENDE
     * Bot leave the Voice Channel, when track is end.
     * If not an new track start.
     *
     * @param player {@link AudioPlayer audio player}
     * @param track {@link AudioTrack audio track}
     * @param endReason {@link AudioTrackEndReason audio track end reason}
     */

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (queue.isEmpty()) {
            voiceChannel.getGuild().getAudioManager().closeAudioConnection();
        } else {
            player.playTrack(queue.element().getTrack());
            queue.remove();
        }
    }
}
