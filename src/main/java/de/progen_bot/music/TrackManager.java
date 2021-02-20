package de.progen_bot.music;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class TrackManager extends AudioEventAdapter
{
    private final AudioPlayer player;
    private final Queue<AudioInfo> queue;
    private final VoiceChannel voiceChannel;

    public TrackManager(AudioPlayer player, VoiceChannel voiceChannel)
    {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
        this.voiceChannel = voiceChannel;
    }

    public void queue(AudioTrack track, Member author)
    {
        final AudioInfo info = new AudioInfo(track, author);
        this.queue.add(info);
        
        if (this.player.getPlayingTrack() == null)
        {
            this.player.playTrack(this.queue.element().getTrack());
            this.queue.remove();
        }
    }

    public Set<AudioInfo> getQueue()
    {
        return new LinkedHashSet<>(this.queue);
    }

    public AudioInfo getInfo(AudioTrack track)
    {
        return this.queue.stream().filter(info -> info.getTrack().equals(track)).findFirst().orElse(null);
    }

    public void purgeQueue()
    {
        this.queue.clear();
    }

    public void shuffleQueue()
    {
        final List<AudioInfo> cQueue = new ArrayList<>(this.getQueue());
        final AudioInfo current = cQueue.get(0);
        
        cQueue.remove(0);
        Collections.shuffle(cQueue);
        cQueue.add(0, current);
        this.purgeQueue();
        this.queue.addAll(cQueue);
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track)
    {
        if (this.voiceChannel == null)
            player.stopTrack();
        else
            this.voiceChannel.getGuild().getAudioManager().openAudioConnection(this.voiceChannel);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason)
    {
        if (this.queue.isEmpty())
            this.voiceChannel.getGuild().getAudioManager().closeAudioConnection();
        else
        {
            player.playTrack(this.queue.element().getTrack());
            this.queue.remove();
        }
    }
}
