package de.progen_bot.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.core.Main;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.music.AudioInfo;
import de.progen_bot.music.Music;
import de.progen_bot.music.PlayerSendHandler;
import de.progen_bot.music.TrackManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class CommandMusic extends CommandHandler {

    public CommandMusic() {
        super("music", "music play <name or link>, music stop, music skip, music resume ", "games music of all kind with progen in your voice channel");
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {

        if (parsedCommand.getArgs().length < 1) {
            super.messageGenerators.generateErrorMsg("Not enough arguments");
            return;
        }
        switch (parsedCommand.getArgs()[0].toLowerCase()) {
            case "play":
            case "p":
                if (parsedCommand.getArgs().length < 2) {

                    super.messageGenerators.generateErrorMsg( "Please enter a valid source!");
                    return;
                }

                String input = Arrays.stream(parsedCommand.getArgs()).skip(1).map(s -> " " + s).collect(Collectors.joining()).substring(1);

                if (!(input.startsWith("http://") || input.startsWith("https://")))
                    input = "ytsearch: " + input;
                Music music = Main.getMusicManager().registerMusicByMember(event.getMember(), new Music(event.getMember(), Main.getMusicBotManager().getUnusedBotForMember(event.getGuild())));
                music.loadTrack(input, event.getMember());
                break;
        }
    }


    @Override
    public String help() {
        return null;
    }

}


