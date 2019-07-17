package de.progen_bot.commands;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class WarnBoard extends CommandHandler {

    public WarnBoard() {
        super("warnboard", "warnboard", "warnboard"); //TODO warnlist desc
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event) {


    }

    @Override
    public String help() {
        return null;
    }
}
