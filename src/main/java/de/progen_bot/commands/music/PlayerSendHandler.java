package de.progen_bot.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;

import java.nio.ByteBuffer;

public class PlayerSendHandler implements AudioSendHandler {


    private final AudioPlayer audioPlayer;

    private AudioFrame lastFrame;


    public PlayerSendHandler(AudioPlayer audioPlayer) {

        this.audioPlayer = audioPlayer;

    }


    @Override

    public boolean canProvide() {

        if (lastFrame == null) {

            lastFrame = audioPlayer.provide();

        }


        return lastFrame != null;

    }


    @Override

    public byte[] provide20MsAudio() {

        if (lastFrame == null) {

            lastFrame = audioPlayer.provide();

        }


        byte[] data = lastFrame != null ? lastFrame.getData() : null;

        lastFrame = null;


        return data;

    }


    @Override

    public boolean isOpus() {

        return true;

    }
}

