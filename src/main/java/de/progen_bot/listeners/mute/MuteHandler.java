package de.progen_bot.listeners.mute;

import de.progen_bot.database.entities.mute.MuteData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.awt.*;

public class MuteHandler extends ListenerAdapter
{
    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event)
    {
        final User sender = event.getAuthor();
        final MuteData data = MuteData.getMuteData(sender.getId());

        if (data != null && data.getGuildId().equals(event.getGuild().getId()))
        {
            event.getMessage().delete().queue();

            sender.openPrivateChannel().queue(pc -> pc.sendMessage(
                    new EmbedBuilder().setColor(Color.orange).setDescription(
                            "You can not write on this server, because you are muted in text channels!\n" +
                                    "Please contact an Supporter, Moderator or Admin at `" + event.getGuild().getName() + "` to unmute.\n\n" +
                                    "Mute Reason: `" + data.getReason() + "`"
                    ).build()
            ).queue(), err ->
            {
                System.err.println("Failed to send message to user");
                err.printStackTrace();
            });
        }
    }
}
