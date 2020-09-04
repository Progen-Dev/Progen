package de.progen_bot.listeners;

import javax.annotation.Nonnull;

import de.progen_bot.core.Main;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MusicListener extends ListenerAdapter {

    @Override
    public void onGuildVoiceLeave(@Nonnull GuildVoiceLeaveEvent event) {
        if (event.getEntity().getUser().isBot()) checkBot(event);
        else checkOwner(event);
    }

    private void checkBot(GuildVoiceLeaveEvent event) {
        VoiceChannel channelOld = event.getOldValue();
        if (Main.getMusicManager().isNotMusicInChannel(channelOld)) return;
        boolean isBot = Main.getMusicManager().getMusicByChannel(channelOld).getBot().getSelfUser().getId().equals(event.getEntity().getId());
        if (isBot) Main.getMusicManager().getMusicByChannel(channelOld).stop();
    }

    private void checkOwner(GuildVoiceLeaveEvent event) {
        boolean isOwner = Main.getMusicManager().isMusicOwner(event.getEntity());
        if (isOwner) Main.getMusicManager().getMusicByOwner(event.getMember()).stop();
    }
}
