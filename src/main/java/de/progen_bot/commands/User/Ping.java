package de.progen_bot.commands.User;

import java.awt.Color;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager.ParsedCommandString;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Ping extends CommandHandler {

	public Ping() {
		super("ping","ping","get the bot ping");
	}

	@Override
	public void execute(ParsedCommandString parsedCommand, MessageReceivedEvent event) {
		System.out.println("[Info] Command pb!ping wird ausgeführt!");
		event.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.green)
				.setDescription("Ping: " + event.getJDA().getPing() + "").build()).queue();
	}

	@Override
	public String help() {
		return null;
	}
}
