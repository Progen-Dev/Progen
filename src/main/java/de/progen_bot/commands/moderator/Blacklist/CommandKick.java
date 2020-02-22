package de.progen_bot.commands.moderator.Blacklist;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.permissions.AccessLevel;
import de.progen_bot.permissions.PermissionCore;
import de.progen_bot.db.entities.config.GuildConfiguration;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;


public class CommandKick extends CommandHandler {
    public CommandKick() {
        super("kick" , "kick <user> <reason>" , "Kick a user from this server");
    }


    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand , MessageReceivedEvent event , GuildConfiguration configuration) {

        String reason = " ";

        if (parsedCommand.getArgs().length > 1) {

            reason = parsedCommand.getArgs()[1];

        }
        event.getTextChannel().sendMessage(

                " " + event.getGuild().getMember(event.getMessage().getMentionedUsers().get(0)).getAsMention() + " got kicked by " + event.getAuthor().getAsMention() + " (" + event.getMember().getRoles().get(0).getName() + ").\n\n" +
                        "Reason: " + reason
        ).queue();

        PrivateChannel pc = event.getMessage().getMentionedUsers().get(0).openPrivateChannel().complete();
        pc.sendMessage(
                "Sorry, you got kicked from Server " + event.getGuild().getName() + " by " + event.getAuthor().getAsMention() + " (" + event.getMember().getRoles().get(0).getName() + ").\n\n" +
                        "Reason: " + reason
        ).queue();

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

    @Override
    public AccessLevel getAccessLevel() {
        return AccessLevel.MODERATOR;
    }
}