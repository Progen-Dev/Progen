package de.progen_bot.commands.User;

import java.awt.Color;

import de.mtorials.config.GuildConfiguration;
import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager.ParsedCommandString;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Ping extends CommandHandler {

	public Ping() {
		super("ping","ping","get the bot ping");
	}

	@Override
	public void execute(ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {
		event.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.green)
				.setDescription("Ping: " + event.getJDA().getPing() + "").build()).queue();
	}

	@Override
	public String help() {
		return null;
	}
}
