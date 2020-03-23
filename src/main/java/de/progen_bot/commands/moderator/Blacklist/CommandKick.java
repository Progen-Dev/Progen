package de.progen_bot.commands.moderator.Blacklist;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.core.PermissionCore;
import de.progen_bot.db.entities.config.GuildConfiguration;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.time.Instant;


public class CommandKick extends CommandHandler {



    public CommandKick() {
        super("kick" , "kick <@user> <reason>" , "Kick a user from this server");
    }


    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand , MessageReceivedEvent event , GuildConfiguration configuration) {
        if (PermissionCore.check(2 , event)) return;
        String reason = "  ";
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
                       .setTimestamp(Instant.now())
                .build())
                .queue();

        event.getGuild().getTextChannelsByName("progenlog",true).get(0).sendMessage(
                new EmbedBuilder()
                        .setColor(Color.MAGENTA)
                        .setTitle("Kick\n")
                        .addField("Victim",
                                event.getGuild().getMember(event.getMessage().getMentionedUsers().get(0)).getAsMention(), true)
                        .addField("Executor",
                                event.getAuthor().getAsMention(),true)
                        .setDescription(event.getMessageId())
                        .addField("Reason", reason, false)
                        .setTimestamp(Instant.now())
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
                       .setTimestamp(Instant.now())
                       .build())
               .queue();

       final Member target = event.getMessage().getMentionedMembers().get(0);

        event.getGuild().kick(target).queue();

    }

    @Override
    public String help() {
        return null;

    }
}