package commands;

import java.awt.Color;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import command.CommandHandler;
import command.CommandManager.ParsedCommandString;
import core.PermissionCore;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Clear extends CommandHandler {

	public Clear() {
		super("clear","clear <anzahl>","clear some messages");
	}

	private int getInt(String string) {
		try {
			return Integer.parseInt(string);
		} catch (Exception exception) {
			exception.printStackTrace();
			return 0;
		}
	}

	@Override
	public void execute(ParsedCommandString parsedCommand, MessageReceivedEvent event) {

		if (PermissionCore.check(1,event))return;

		String[] args = parsedCommand.getArgs();
		int numb = getInt(args[0]);

		if (args.length < 1) {
			event.getTextChannel().sendMessage(error
					.setDescription("Bitte benutze die Anzahl von Nachrichten, welche gelöscht werden sollen").build())
					.queue();
		}

		if (numb > 1 && numb <= 100) {
			try {
				MessageHistory history = new MessageHistory(event.getTextChannel());
				List<Message> msgs;
				event.getMessage().delete().queue();

				msgs = history.retrievePast(numb).complete();
				event.getTextChannel().deleteMessages(msgs).queue();

				if (msgs.size() < Integer.parseInt(args[0])) {
					Message msg = event.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.ORANGE)
							.setDescription("Es konnten nur " + msgs.size() + " Nachrichten gelöscht werden!").build())
							.complete();

					new Timer().schedule(new TimerTask() {
						@Override
						public void run() {
							msg.delete().queue();
						}
					}, 3000);
				} else {
					Message msg = event.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.GREEN)
							.setDescription(msgs.size() + " gelöschte Nachrichten").build()).complete();

					new Timer().schedule(new TimerTask() {
						@Override
						public void run() {
							msg.delete().queue();
						}
					}, 3000);
				}

				System.out.println("[INFO]: Es wurden " + msgs.size() + " Nachrichten gelöscht");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			event.getTextChannel()
					.sendMessage(error.setDescription("Bitte benutze eine Zahl zwischen 2 und 100!").build()).queue();
		}
	}

	@Override
	public String help() {
		return null;
	}
}
