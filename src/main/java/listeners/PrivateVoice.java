package listeners;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class PrivateVoice extends ListenerAdapter {
    public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
        if (event.getChannelJoined().getName().contains("🇵")) {
            event.getGuild().getController().createVoiceChannel("[PRIVAT] " + event.getMember().getUser().getName()).complete().createPermissionOverride(event.getGuild().getPublicRole()).setDeny(Permission.VOICE_CONNECT).queue();
            event.getGuild().getVoiceChannelsByName("[PRIVAT] " + event.getMember().getUser().getName(), false).get(0).createPermissionOverride(event.getMember()).setAllow(Permission.VOICE_CONNECT).queue();
            event.getGuild().getController().moveVoiceMember(event.getMember(), event.getGuild().getVoiceChannelsByName("[PRIVAT] " + event.getMember().getUser().getName(), false).get(0)).queue();
        }
    }

    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
        if (event.getChannelLeft().getName().contains("[PRIVAT] ") && event.getChannelLeft().getMembers().size() == 0) {
            event.getChannelLeft().delete().complete();
            return;
        }
    }
    public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
        if (event.getChannelJoined().getName().contains("🇵")) {
            event.getGuild().getController().createVoiceChannel("[PRIVAT] " + event.getMember().getUser().getName()).complete().createPermissionOverride(event.getGuild().getPublicRole()).setDeny(Permission.VOICE_CONNECT).queue();
            event.getGuild().getVoiceChannelsByName("[PRIVAT] " + event.getMember().getUser().getName(), false).get(0).createPermissionOverride(event.getMember()).setAllow(Permission.VOICE_CONNECT).queue();
            event.getGuild().getController().moveVoiceMember(event.getMember(), event.getGuild().getVoiceChannelsByName("[PRIVAT] " + event.getMember().getUser().getName(), false).get(0)).queue();
            return;
        }
        if (event.getChannelLeft().getName().contains("[PRIVAT] ") && event.getChannelLeft().getMembers().size() == 0) {
            event.getChannelLeft().delete().complete();
            return;
        }
    }
}
