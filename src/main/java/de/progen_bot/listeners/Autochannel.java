package de.progen_bot.listeners;

import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;


public class Autochannel extends ListenerAdapter {

    @Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
        VoiceChannel vc = event.getChannelJoined();

        if (vc.getName().contains("⏬")) {
            VoiceChannel vc1 = (VoiceChannel) event.getGuild().getController().createVoiceChannel(vc.getName().replace("⏬","⏫"))
                    .setBitrate(vc.getBitrate())
                    .setUserlimit(vc.getUserLimit())
                    .complete();

            if (vc.getParent() != null)
                vc1.getManager().setParent(vc.getParent()).queue();

            event.getGuild().getController().modifyVoiceChannelPositions().selectPosition(vc1).moveTo(vc.getPosition() + 1).queue();
            event.getGuild().getController().modifyVoiceChannelPositions().selectPosition(vc1).moveTo(vc.getPosition() + 1).queue();
            event.getGuild().getController().moveVoiceMember(event.getMember(), vc1).queue();
        }
    }

    @Override
    public void onGuildVoiceMove(GuildVoiceMoveEvent event) {

        VoiceChannel vc = event.getChannelJoined();

        if (vc.getName().contains("⏬")) {
            VoiceChannel vc1 = (VoiceChannel) event.getGuild().getController().createVoiceChannel(vc.getName().replace("⏬","⏫"))
                    .setBitrate(vc.getBitrate())
                    .setUserlimit(vc.getUserLimit())
                    .complete();

            if (vc.getParent() != null)
                vc1.getManager().setParent(vc.getParent()).queue();

            event.getGuild().getController().modifyVoiceChannelPositions().selectPosition(vc1).moveTo(vc.getPosition() + 1).queue();
            event.getGuild().getController().moveVoiceMember(event.getMember(), vc1).queue();
        }

        vc = event.getChannelLeft();

        if (vc.getName().contains("⏫") && vc.getMembers().size() == 0) {
            vc.delete().queue();
        }
    }

    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
        VoiceChannel vc = event.getChannelLeft();

        if (vc.getName().contains("⏫") && vc.getMembers().size() == 0) {
            vc.delete().queue();
        }
    }

}
