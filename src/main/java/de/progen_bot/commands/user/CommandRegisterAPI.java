package de.progen_bot.commands.user;

import de.mtorials.pwi.httpapi.API;
import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.permissions.AccessLevel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandRegisterAPI extends CommandHandler {

    public CommandRegisterAPI() {
        super("register", "register", "The usage of this command is shown in the web interface.");
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {
        if (event.getMember() == null)
            return;

        String token = API.getTokenManager().register(event.getMember());
        event.getMember().getUser().openPrivateChannel().complete().sendMessage("This is your new token only for the guild `" + event.getGuild().getName() + "` : ||" +
                token + "||" + "\n*Go to <http://pwi.progen-bot.de> to sign in*\n").queue();
    }

    @Override
    public AccessLevel getAccessLevel() {
        return AccessLevel.USER;
    }
}
