package de.progen_bot.listeners;

import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Joinlog extends ListenerAdapter {

    public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {

        event.getGuild().getTextChannelsByName("progenlog" , true).get(0).sendMessage(

                "Member " + event.getVoiceState().getMember().getUser().getName() + " joined voice channel " + event.getChannelJoined().getName() + "."

        ).queue();

    }
}



