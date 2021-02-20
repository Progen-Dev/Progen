package de.progen_bot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;

import java.nio.ByteBuffer;

public class PlayerSendHandler implements AudioSendHandler
{
    private final AudioPlayer player;
    private AudioFrame lastFrame;

    public PlayerSendHandler(AudioPlayer player)
    {
        this.player = player;
    }

    @Override
    public boolean canProvide()
    {
        if (this.lastFrame == null)
            this.lastFrame = this.player.provide();

        return this.lastFrame != null;
    }

    @Override
    public ByteBuffer provide20MsAudio()
    {
        if (this.lastFrame == null)
            this.lastFrame = this.player.provide();

        final byte[] data = this.lastFrame != null ? this.lastFrame.getData() : null;

        return ByteBuffer.wrap(data);
    }

    @Override
    public boolean isOpus()
    {
        return true;
    }
}
