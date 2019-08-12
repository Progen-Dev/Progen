package de.progen_bot.core;

import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.nio.channels.Channel;

public class TempChannelController {

    private Category category;

    public TempChannelController(Category category) {

        this.category = category;
    }

    public Channel createTextChannel(String name, int userlimit) {

        Channel channel =  category.createTextChannel(name).complete();
        channel.getManager().setParent(category).queue();
        channel.getManager().setUserLimit(userlimit).queue();
        return channel;
    }

    public Channel createTextChannel(String name) {

        Channel channel =  category.createTextChannel(name).complete();
        channel.getManager().setParent(category).queue();
        return channel;
    }


    public int getTempChannelCount() {

        return category.getVoiceChannels().size();
    }

    public void removeOldestTempChannel() {

        VoiceChannel voiceChannel = category.getVoiceChannels().get(0);
        voiceChannel.delete().queue();
    }
}
