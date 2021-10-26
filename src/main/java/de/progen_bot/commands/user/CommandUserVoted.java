package de.progen_bot.commands.user;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.core.Main;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.permissions.AccessLevel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandUserVoted extends CommandHandler {

    public CommandUserVoted() {
        super("hasvoted", "`hasVoted <user>`", "Shows if a user has voted for Progen");
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {

        User user;
        try {
            user = event.getMessage().getMentionedMembers().get(0).getUser();
        } catch (IndexOutOfBoundsException e) {
            event.getTextChannel().sendMessageEmbeds(super.messageGenerators.generateErrorMsgWrongInput()).queue();
            return;
        }
        Main.getTopGG().hasVoted(user, hasVoted -> {
            String msg;
            if (hasVoted) msg = "User " + user.getName() + " has votet! Thank you!";
            else msg = "User "+ user.getName() + " has not voted yet!\n\n" +
                    "Vote now for Progen!: <https://top.gg/bot/495293590503817237>";
            event.getTextChannel().sendMessage(msg).queue();
        });
    }

    @Override
    public AccessLevel getAccessLevel() {
        return AccessLevel.USER;
    }
}
