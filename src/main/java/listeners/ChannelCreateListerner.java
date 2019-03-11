package listeners;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.core.events.channel.voice.VoiceChannelCreateEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class ChannelCreateListerner extends ListenerAdapter {
    public void onTextChannelCreate(TextChannelCreateEvent event) {
        if (event.getGuild().getRolesByName("blue-muted",false).size()== 0){
            return;
        }
        event.getChannel().createPermissionOverride(event.getGuild().getRolesByName("blue-muted",false).get(0)).setDeny(Permission.MESSAGE_WRITE).complete();
    }
    public void onVoiceChannelCreate(VoiceChannelCreateEvent event2) {
        if (event2.getGuild().getRolesByName("blue-muted",false).size()== 0){
            return;
        }
        event2.getChannel().createPermissionOverride(event2.getGuild().getRolesByName("blue-muted",false).get(0)).setDeny(Permission.VOICE_SPEAK).complete();
    }
}
