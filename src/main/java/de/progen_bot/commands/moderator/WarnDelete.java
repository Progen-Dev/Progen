package de.progen_bot.commands.moderator;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.db.dao.warnlist.WarnListDaoImpl;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.permissions.AccessLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.Color;
import java.util.Arrays;

public class WarnDelete extends CommandHandler {
    public WarnDelete() {
        super("warndelete", "warndelete <user> <reason>", "Delete warn of a user");
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event,
            GuildConfiguration configuration) {

        if (event.getMember() == null)
            return;

        if (event.getMessage().getMentionedMembers().isEmpty()) {
            event.getTextChannel().sendMessage(super.messageGenerators.generateErrorMsg("No user found")).queue();
            return;
        }

        if (parsedCommand.getArgs().length <= 1) {
            event.getChannel()
                    .sendMessage(new EmbedBuilder().setColor(Color.red).setDescription("No reason found").build())
                    .queue();
            return;
        }

        String reason = String.join(" ", Arrays.copyOfRange(parsedCommand.getArgs(), 1, parsedCommand.getArgs().length));

        new WarnListDaoImpl().deleteWarn(event.getMember(), reason);

        event.getTextChannel().sendMessage(super.messageGenerators.generateSuccessfulMsg()).queue();
    }

    @Override
    public AccessLevel getAccessLevel() {
        return AccessLevel.MODERATOR;
    }
}