package de.progen_bot.commands;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.util.Settings;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandJoinMessage extends CommandHandler {

    /**
     * Instantiates a new de.progen_bot.command handler.
     *
     * @param invokeString the invoke string
     * @param commandUsage the de.progen_bot.command usage
     * @param description  the description
     */
    public CommandJoinMessage(String invokeString , String commandUsage , String description) {
        super(invokeString , commandUsage , description);
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand , MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return null;
    }
}
