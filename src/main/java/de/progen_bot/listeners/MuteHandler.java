package de.progen_bot.listeners;

import de.progen_bot.db.entities.MuteData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

public class MuteHandler extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        final User sender = e.getAuthor();
        final MuteData data = MuteData.getMuteData(sender.getId());
        if (data != null && data.getGuildId().equals(e.getGuild().getId())) {
            sender.openPrivateChannel().queue(pc -> pc.sendMessage(
                    new EmbedBuilder().setColor(Color.orange).setDescription(
                            "You can not write on this server, because you are muted in text channels!\n" +
                                    "Please contact an Supporter, Moderator or Admin at `" + e.getGuild().getName() + "` to unmute.\n\n" +
                                    "Mute Reason: `" + data.getReason() + "`"
                    ).build()
            ).queue());
            e.getMessage().delete().queue();
        }
    }

}