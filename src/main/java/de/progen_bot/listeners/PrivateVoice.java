package de.progen_bot.listeners;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.naming.PartialResultException;

public class PrivateVoice extends ListenerAdapter {

    public static final String PRIVATEVOICECHANNELPREFIX = "[P]";

    public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
        if (!event.getChannelJoined().getName().contains(PRIVATEVOICECHANNELPREFIX)) return;

    }

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
