package de.progen_bot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import de.progen_bot.core.Main;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackManager extends AudioEventAdapter {
    private final AudioPlayer PLAYER;
    private Queue<AudioInfo> queue;
    private final VoiceChannel voiceChannel;
    private final JDA bot;
    private final Member owner;

    /**
     * Erstellt eine Instanz der Klasse TrackManager.
     *  @param player
     * @param bot
     * @param owner
     */

    public TrackManager(AudioPlayer player, VoiceChannel voiceChannel, JDA bot, Member owner) {
        this.PLAYER = player;
        this.bot = bot;
        this.owner = owner;
        this.queue = new LinkedBlockingQueue<>();
        this.voiceChannel = voiceChannel;
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
        if (PLAYER.getPlayingTrack() == null) {
            PLAYER.playTrack(track);
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

        Queue<AudioInfo> newqueue = new LinkedBlockingQueue<>();
        newqueue.add(queue.element());
        queue = newqueue;
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
        AudioInfo info = queue.element();
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
        Guild g = queue.poll().getAuthor().getGuild();
        if (queue.isEmpty()) {
            g.getAudioManager().closeAudioConnection();
            Main.getMusicBotManager().setBotUnsed(voiceChannel.getGuild(), bot);
            Main.getMusicManager().unregisterMusicByOwner(owner);
        } else
            player.playTrack(queue.element().getTrack());
    }
}
