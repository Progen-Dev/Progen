package de.progen_bot.listeners;

import de.progen_bot.core.Main;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.DisconnectEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class BotDisconnectListener extends ListenerAdapter {

    @Override
    public void onGuildVoiceLeave(@Nonnull GuildVoiceLeaveEvent event) {
        VoiceChannel channelOld = event.getOldValue();
        if (!Main.getMusicManager().isMusicInChannel(channelOld)) return;
        if (!event.getEntity().getUser().isBot()) return;

        boolean isRightBot = Main.getMusicManager().getMusicByChannel(channelOld).getBot().getSelfUser().getId().equals(event.getEntity().getId());
        if (isRightBot) Main.getMusicManager().getMusicByChannel(channelOld).stop();
    }
}
