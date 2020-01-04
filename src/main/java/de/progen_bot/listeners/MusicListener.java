package de.progen_bot.listeners;

import de.progen_bot.core.Main;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.DisconnectEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class MusicListener extends ListenerAdapter {

    @Override
    public void onGuildVoiceLeave(@Nonnull GuildVoiceLeaveEvent event) {
        VoiceChannel channelOld = event.getOldValue();
        if (!Main.getMusicManager().isMusicInChannel(channelOld)) return;
        if (event.getEntity().getUser().isBot()) checkBot(event);
        else checkOwner(event);
    }

    private void checkBot(GuildVoiceLeaveEvent event) {
        VoiceChannel channelOld = event.getOldValue();
        boolean isRightBot = Main.getMusicManager().getMusicByChannel(channelOld).getBot().getSelfUser().getId().equals(event.getEntity().getId());
        if (isRightBot) Main.getMusicManager().getMusicByChannel(channelOld).stop();
    }

    private void checkOwner(GuildVoiceLeaveEvent event) {
        VoiceChannel channelOld = event.getOldValue();
        boolean isOwner = Main.getMusicManager().getMusicByChannel(channelOld).getOwner().getId().equals(event.getEntity().getId());
        if (isOwner) Main.getMusicManager().getMusicByChannel(channelOld).stop();
    }
}
