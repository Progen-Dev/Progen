package de.progen_bot.commands.Administartor;

import de.mtorials.webinterface.httpapi.API;
import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandRegisterAPI extends CommandHandler {

    public CommandRegisterAPI() {
        super("register", "regitser", "The usage of this command is shown in the web interface.");
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event) {
        System.out.println("[Info] Command pb!register wird ausgeführt!");

        String token = API.getTokenManager().register(event.getMember());
        event.getMember().getUser().openPrivateChannel().complete().sendMessage("This is your new token: " + token).queue();
    }

    @Override
    public String help() {
        return null;
    }
}
