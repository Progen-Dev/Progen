package de.progen_bot.commands.User;

import de.mtorials.config.GuildConfiguration;
import de.mtorials.webinterface.httpapi.API;
import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandRegisterAPI extends CommandHandler {

    public CommandRegisterAPI() {
        super("register", "regitser", "The usage of this command is shown in the web interface.");
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {

        String token = API.getTokenManager().register(event.getMember());
        event.getMember().getUser().openPrivateChannel().complete().sendMessage("This is your new token: ```" + token + "```Go to http://progen-bot/pwi/ to sign in.").queue();
    }

    @Override
    public String help() {
        return null;
    }
}
