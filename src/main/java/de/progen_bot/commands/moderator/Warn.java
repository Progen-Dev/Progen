package de.progen_bot.commands.moderator;

import java.awt.Color;
import java.time.Instant;
import java.util.List;
import java.util.Arrays;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager.ParsedCommandString;
import de.progen_bot.db.dao.warnlist.WarnListDaoImpl;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.permissions.AccessLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;


public class Warn extends CommandHandler {
    private static final String WARN = "WARN";
    private static final String EXECUTOR = "Executor";
    private static final String VICTIM = "Victim";
    private static final String REASON = "Reason";

    public Warn() {
        super("warn", "warn <user> <reason>", "Warn a user");
    }

    private MessageEmbed getWarnEmbed(MessageReceivedEvent event, String reason){
        return new EmbedBuilder()
            .setColor(Color.ORANGE)
            .setTitle(WARN)
            .addField(VICTIM, event.getMessage().getMentionedUsers().get(0).getAsMention(), true)
            .addField(EXECUTOR, event.getMember().getAsMention(), true)
            .setDescription(event.getMessageId())
            .addField(REASON, reason, false)
            .setTimestamp(Instant.now()).build();
    }

    @Override
    public void execute(ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {

        Member warned;
        if (event.getMessage().getMentionedUsers().size() == 1) {
            warned = event.getMessage().getMentionedMembers().get(0);
        } else {
            event.getChannel().sendMessage(new EmbedBuilder().setColor(Color.red).setDescription("No user found").build())
                    .queue();
            return;
        }

        if (parsedCommand.getArgs().length <= 1) {
            event.getChannel().sendMessage(new EmbedBuilder().setColor(Color.red).setDescription("No reason found").build())
                    .queue();
            return;
        }

        String reason = String.join(" ", Arrays.copyOfRange(parsedCommand.getArgs(), 1, parsedCommand.getArgs().length));

        final MessageEmbed eb = getWarnEmbed(event, reason);
            if(eb == null)
            return;
        
        event.getChannel().sendMessage(eb).queue();
        final List<TextChannel> channel = event.getGuild().getTextChannelsByName("progenlog", true);
        
        if(!channel.isEmpty())
            channel.get(0).sendMessage(eb).queue();

        WarnListDaoImpl dao = new WarnListDaoImpl();
        dao.insertWarn(warned, reason);
    }

    @Override
    public AccessLevel getAccessLevel() {
        return AccessLevel.MODERATOR;
    }
}
