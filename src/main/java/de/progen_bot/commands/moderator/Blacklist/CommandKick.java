package de.progen_bot.commands.moderator.Blacklist;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.core.PermissionCore;
import de.progen_bot.db.entities.config.GuildConfiguration;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.net.ntp.TimeStamp;

import java.awt.*;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;


public class CommandKick extends CommandHandler {
    public CommandKick() {
        super("kick" , "kick <user> <reason>" , "Kick a user from this server");
    }


    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand , MessageReceivedEvent event , GuildConfiguration configuration) {
        if (PermissionCore.check(2 , event)) return;
        Message message = event.getMessage();
        String reason = " ";
        if (parsedCommand.getArgs().length > 1) {
            reason = parsedCommand.getArgs()[1];
        }

        event.getTextChannel().sendMessage(
               new EmbedBuilder()
                       .setColor(Color.MAGENTA)
                       .setTitle("Kick\n")
                       .addField("Victim",
                               event.getAuthor().getAsMention(), true)
                       .addField("Executor",
                               event.getGuild().getMember(event.getMessage().getMentionedUsers().get(0)).getAsMention(),true)
                       .setDescription(event.getMessageId())
                       .addField("Reason", reason, false)
                .build())
                .queue();

        event.getGuild().getTextChannelsByName("progenlog",true).get(0).sendMessage(
                new EmbedBuilder()
                        .setColor(Color.MAGENTA)
                        .setTitle("Kick\n")
                        .addField("Victim",
                                event.getAuthor().getAsMention(),true)
                        .addField("Executor",
                                event.getGuild().getMember(event.getMessage().getMentionedUsers().get(0)).getAsMention(),true)
                        .setDescription(event.getMessageId())
                        .addField("Reason", reason, false)
                .build()
        ).queue();

        PrivateChannel pc = event.getMessage().getMentionedUsers().get(0).openPrivateChannel().complete();
       pc.sendMessage(
               new EmbedBuilder()
                       .setColor(Color.MAGENTA)
                       .setTitle("Kick\n")
                       .addField("Victim",
                               event.getAuthor().getAsMention(), true)
                       .addField("Executor",
                               event.getGuild().getMember(event.getMessage().getMentionedUsers().get(0)).getAsMention(),true)
                       .setDescription(event.getMessageId())
                       .addField("Reason", reason, false)
                       .build())
               .queue();


        event.getGuild().kick(
                event.getGuild().getMember(
                        event.getMessage().getMentionedUsers().get(0)
                )
        ).queue();

    }

    @Override
    public String help() {
        return null;

    }
}