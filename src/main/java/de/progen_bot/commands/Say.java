package de.progen_bot.commands;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager.ParsedCommandString;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Say extends CommandHandler {

	public Say() {
		super("say","say <text>","let the bot write a message");
	}

	@Override
	public void execute(ParsedCommandString parsedCommand, MessageReceivedEvent event) {
	    String out = " ";
        for ( String s : parsedCommand.getArgs()) {
            out += s + " ";
        }

        event.getTextChannel().sendMessage(out).queue();
	}

	@Override
	public String help() {
		return null;
	}

}
