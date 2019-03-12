package de.progen_bot.listeners;

import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Leavelog extends ListenerAdapter {
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {

        event.getGuild().getTextChannelsByName("progenlog" , true).get(0).sendMessage(

                "Member " + event.getVoiceState().getMember().getUser().getName() + " Leaved voice channel " + event.getChannelLeft().getName() + "."

        ).queue();

    }
}



