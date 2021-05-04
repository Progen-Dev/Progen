package de.progen_bot.listeners.autochannel;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class AutoChannelListener extends ListenerAdapter
{

    private static VoiceChannel createVoiceChannel(VoiceChannel vc, Guild guild)
    {
        VoiceChannel vc1 = guild.createVoiceChannel(vc.getName().replace("⏬", "⏫"))
                .setBitrate(vc.getBitrate())
                .setUserlimit(vc.getUserLimit())
                .complete();

        if (vc.getParent() != null)
            vc1.getManager().setParent(vc.getParent()).queue();

        guild.modifyVoiceChannelPositions().selectPosition(vc1).moveTo(vc.getPosition() + 1).queue();
        return vc1;
    }

    @Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent event)
    {
        VoiceChannel vc = event.getChannelJoined();

        if (vc.getName().contains("⏬"))
        {
            VoiceChannel vc1 = createVoiceChannel(vc, event.getGuild());
            event.getGuild().modifyVoiceChannelPositions().selectPosition(vc1).moveTo(vc.getPosition() + 1).queue();
            event.getGuild().moveVoiceMember(event.getMember(), vc1).queue();
        }
    }

    @Override
    public void onGuildVoiceMove(GuildVoiceMoveEvent event)
    {

        VoiceChannel vc = event.getChannelJoined();

        if (vc.getName().contains("⏬"))
        {
            VoiceChannel vc1 = createVoiceChannel(vc, event.getGuild());
            event.getGuild().moveVoiceMember(event.getMember(), vc1).queue();
        }

        vc = event.getChannelLeft();

        if (vc.getName().contains("⏫") && vc.getMembers().isEmpty())
        {
            vc.delete().queue();
        }
    }

    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event)
    {
        VoiceChannel vc = event.getChannelLeft();

        if (vc.getName().contains("⏫") && vc.getMembers().isEmpty())
        {
            vc.delete().queue();
        }
    }

}
