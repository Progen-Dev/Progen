package de.progen_bot.listeners.music;

import de.progen_bot.core.Main;
import de.progen_bot.music.MusicManager;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class MusicListener extends ListenerAdapter
{
    private final MusicManager manager = Main.getMusicManager();

    @Override
    public void onGuildVoiceLeave(@Nonnull GuildVoiceLeaveEvent event)
    {
        if (event.getEntity().getUser().isBot())
            this.checkBot(event);
        else
            this.checkOwner(event);
    }

    private void checkOwner(GuildVoiceLeaveEvent event)
    {
        final boolean isOwner = this.manager.isMusicOwner(event.getEntity());
        if (isOwner)
            this.manager.getMusicByOwner(event.getEntity()).stop();
    }

    private void checkBot(GuildVoiceLeaveEvent event)
    {
        final VoiceChannel old = event.getChannelLeft();
        if (this.manager.isNotMusicInChannel(old))
            return;

        final boolean isBot = this.manager.getMusicByChannel(old).getBot().getSelfUser().getIdLong() == event.getEntity().getIdLong();
        if (isBot)
            this.manager.getMusicByChannel(old).stop();
    }
}
