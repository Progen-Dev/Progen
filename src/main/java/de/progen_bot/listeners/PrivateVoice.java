package de.progen_bot.listeners;


import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


public class PrivateVoice extends ListenerAdapter {

    public static final String PRIVATEVOICECHANNELPREFIX = "[P]";

    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {

        if (!event.getChannelLeft().getName().contains(PRIVATEVOICECHANNELPREFIX)) return;
        if (event.getChannelLeft().getMembers().size() == 0) {
            event.getChannelLeft().delete().complete();
        }
    }

    public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
        if (!event.getChannelLeft().getName().contains(PRIVATEVOICECHANNELPREFIX)) return;
        if (event.getChannelLeft().getMembers().size() == 0) {
            event.getChannelLeft().delete().complete();
        }
    }
}
