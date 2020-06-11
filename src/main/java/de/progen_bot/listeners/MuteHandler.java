package de.progen_bot.listeners;

import de.progen_bot.commands.moderator.CommandMute;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.Map;

public class MuteHandler extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        final User sender = e.getAuthor();
        final Map<String, String> mutes = CommandMute.getMuted();
        if (mutes.containsKey(sender.getId())) {
            sender.openPrivateChannel().queue(pc -> pc.sendMessage(
                    new EmbedBuilder().setColor(Color.orange).setDescription(
                            "You can not write on this server, because you are muted in text channels!\n" +
                                    "Please contact an Supporter, Moderator or Admin to unmute.\n\n" +
                                    "Mute Reason: `" + mutes.get(sender.getId()) + "`"
                    ).build()
            ).queue());
            e.getMessage().delete().queue();
        }
    }

}