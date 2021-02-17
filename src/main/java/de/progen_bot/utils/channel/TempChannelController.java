package de.progen_bot.utils.channel;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.List;
import java.util.function.Consumer;

public class TempChannelController
{
    private final Category category;

    public TempChannelController(Category category)
    {
        this.category = category;
    }

    public void create(String name, int userLimit, Consumer<VoiceChannel> textChannel)
    {
        if (!category.getGuild().getSelfMember().hasPermission(this.category, Permission.MANAGE_CHANNEL))
            textChannel.accept(null);

        category.createVoiceChannel(name).setUserlimit(userLimit).queue(textChannel);
    }

    public void create(String name, Consumer<VoiceChannel> textChannel)
    {
        this.create(name, 0, textChannel);
    }

    public int getTempChannelCount()
    {
        return this.category.getVoiceChannels().size();
    }

    public void removeOldestTempChannel()
    {
        final List<VoiceChannel> channels = this.category.getVoiceChannels();

        if (channels.isEmpty())
            return;

        channels.get(0).delete().queue();
    }
}
