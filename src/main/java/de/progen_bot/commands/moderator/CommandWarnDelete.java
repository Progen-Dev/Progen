package de.progen_bot.commands.moderator;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.core.Main;
import de.progen_bot.db.dao.warnlist.WarnListDaoImpl;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.permissions.AccessLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.time.Instant;
import java.util.Arrays;

public class CommandWarnDelete extends CommandHandler {
    private static final String DELETE = "Warn Delete";
    private static final String EXECUTOR = "Executor";
    private static final String PARDON = "Member";
    private static final String WARN = "Warn";
    public CommandWarnDelete() {
        super("warndelete", "warndelete <user> <reason>", "Delete warn of a user");
    }

    private MessageEmbed getDeleteEmbed(MessageReceivedEvent event, String reason){
        return new EmbedBuilder()
        .setColor(Color.green)
        .setTitle(DELETE)
        .addField(PARDON, event.getMessage().getMentionedUsers().get(0).getAsMention(), true)
        .addField(EXECUTOR, event.getMember().getAsMention(), true)
        .setDescription(event.getMessageId())
        .addField(WARN, reason, false)
        .setTimestamp(Instant.now())
        .build();
    }


    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event,
            GuildConfiguration configuration) {

        TextChannel getWarnDeleteChannel = Main.getJda().getTextChannelById(configuration.getLogChannelID());

        if (event.getMember() == null)
            return;

        if (event.getMessage().getMentionedMembers().isEmpty()) {
            event.getTextChannel().sendMessageEmbeds(super.messageGenerators.generateErrorMsg("No user found")).queue();
            return;
        }

        if (parsedCommand.getArgs().length <= 1) {
            event.getChannel()
                    .sendMessageEmbeds(new EmbedBuilder().setColor(Color.red).setDescription("No reason found").build())
                    .queue();
            return;
        }

        String reason = String.join(" ", Arrays.copyOfRange(parsedCommand.getArgs(), 1, parsedCommand.getArgs().length));

        new WarnListDaoImpl().deleteWarn(event.getMember(), reason);

        final MessageEmbed eb = getDeleteEmbed(event, reason);
        if(eb == null)
        return;

        getWarnDeleteChannel.sendMessageEmbeds(eb).queue();
    }

    @Override
    public AccessLevel getAccessLevel() {
        return AccessLevel.MODERATOR;
    }
}
