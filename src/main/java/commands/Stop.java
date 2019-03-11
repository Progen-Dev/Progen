package commands;

import java.awt.Color;
import java.util.Timer;
import java.util.TimerTask;

import command.CommandHandler;
import command.CommandManager.ParsedCommandString;
import core.PermissionCore;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Stop extends CommandHandler {

	public Stop() {
		super("stop","stop","stops the bot");
	}

	@Override
	public void execute(ParsedCommandString parsedCommand, MessageReceivedEvent event) {

		if (PermissionCore.check(4,event))return;

		event.getMessage().delete().queue();

		Message msg = event.getTextChannel()
				.sendMessage(new EmbedBuilder().setColor(Color.red).setDescription("Fahre herunter...").build())
				.complete();

		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				msg.delete().queue();
				System.exit(0);
			}
		}, 3000);
	}
	@Override
	public String help() {
		return null;
	}
}
