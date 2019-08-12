package de.progen_bot.commands.Moderator;

import java.awt.Color;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.mtorials.config.GuildConfiguration;
import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager.ParsedCommandString;
import de.progen_bot.core.PermissionCore;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;


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
	public void execute(ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {

		if (PermissionCore.check(1,event))return;

		String[] args = parsedCommand.getArgs();
		int numb = getInt(args[0]);

		if (args.length < 1) {
			event.getTextChannel().sendMessage(super.messageGenerators.generateErrorMsg("Please specify the count of messages to be removed."))
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
					.sendMessage(super.messageGenerators.generateErrorMsg("Please use a Number between HBA ICH VERGESSEN")).queue();
		}
	}

	@Override
	public String help() {
		return null;
	}
}
