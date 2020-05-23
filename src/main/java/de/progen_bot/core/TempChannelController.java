package de.progen_bot.core;

import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class TempChannelController {

    private final Category category;

    public TempChannelController(Category category) {

        this.category = category;
    }

    public void createTextChannel(String name, int userlimit) {

        TextChannel channel = category.createTextChannel(name).complete();
        channel.getManager().setParent(category).queue();
        channel.getManager().setUserLimit(userlimit).queue();
	}

    public void createTextChannel(String name) {

        TextChannel channel = category.createTextChannel(name).complete();
        channel.getManager().setParent(category).queue();
	}


    public int getTempChannelCount() {

        return category.getVoiceChannels().size();
    }

    public void removeOldestTempChannel() {

        VoiceChannel voiceChannel = category.getVoiceChannels().get(0);
        voiceChannel.delete().queue();
    }
}
