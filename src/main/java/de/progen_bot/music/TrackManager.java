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
    private Queue<AudioInfo> queue;
    private final VoiceChannel voiceChannel;
    //rivate final Music music;

    /**
     * Erstellt eine Instanz der Klasse TrackManager.
     * @param player
     *
     */

    public TrackManager(AudioPlayer player, VoiceChannel voiceChannel) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
        this.voiceChannel = voiceChannel;
        //this.music = music;
    }

    /**
     * Reiht den übergebenen Track in die Queue ein.
     *
     * @param track  AudioTrack
     * @param author Member, der den Track eingereiht hat
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
     * Returnt die momentane Queue als LinkedHashSet.
     *
     * @return Queue
     */

    public Set<AudioInfo> getQueue() {
        return new LinkedHashSet<>(queue);
    }

    /**
     * Returnt AudioInfo des Tracks aus der Queue.
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
     * Leert die gesammte Queue.
     */

    public void purgeQueue() {

        queue.clear();
    }


    /**
     * Shufflet die momentane Queue.
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
     * PLAYER EVENT: TRACK STARTET
     * Wenn Einreiher nicht im VoiceChannel ist, wird der Player gestoppt.
     * Sonst connectet der Bot in den Voice Channel des Einreihers.
     *
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
     * Wenn die Queue zuende ist, verlässt der Bot den Audio Channel.
     * Sonst wird der nächste Track in der Queue wiedergegeben.
     *
     * @param player
     * @param track
     * @param endReason
     */

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (queue.isEmpty()) {
            System.out.println("Queue is empty!");
        } else {
            player.playTrack(queue.element().getTrack());
            queue.remove();
        }
    }
}
