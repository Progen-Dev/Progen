package de.progen_bot.listeners.voice;

import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class PrivateVoice extends ListenerAdapter
{
    public static final String PREFIX = "[P]";

    @Override
    public void onGuildVoiceLeave(@Nonnull GuildVoiceLeaveEvent event)
    {
        if (!event.getChannelLeft().getName().contains(PREFIX))
            return;
        if (event.getChannelLeft().getMembers().isEmpty())
            event.getChannelLeft().delete().queue();
    }

    @Override
    public void onGuildVoiceMove(@Nonnull GuildVoiceMoveEvent event)
    {
        if (!event.getChannelLeft().getName().contains(PREFIX))
            return;
        if (!event.getChannelLeft().getMembers().isEmpty())
            event.getChannelLeft().delete().queue();
    }
}
