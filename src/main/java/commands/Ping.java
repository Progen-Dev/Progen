package commands;

import java.awt.Color;

import command.CommandHandler;
import command.CommandManager.ParsedCommandString;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Ping extends CommandHandler {

	public Ping() {
		super("ping","ping","get the bot ping");
	}

	@Override
	public void execute(ParsedCommandString parsedCommand, MessageReceivedEvent event) {
		event.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.green)
				.setDescription("Ping: " + event.getJDA().getPing() + "").build()).queue();
	}

	@Override
	public String help() {
		return null;
	}
}
