package commands;

import java.awt.Color;

import command.CommandHandler;
import command.CommandManager.ParsedCommandString;
import core.PermissionCore;
import db.MySQL;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Warn extends CommandHandler {

	public Warn() {
		super("warn","warn <user> <reason>","warned a user");
	}

	@Override
	public void execute(ParsedCommandString parsedCommand, MessageReceivedEvent event) {
		if (PermissionCore.check(3,event))return;

		User warned = null;
		if (event.getMessage().getMentionedUsers().size() == 1) {
			warned = event.getMessage().getMentionedUsers().get(0);
		} else {
			event.getChannel().sendMessage(new EmbedBuilder().setColor(Color.red).setDescription("No user found").build())
					.queue();
			return;
		}

		String reason = String.join(" ", parsedCommand.getArgs()).replace(parsedCommand.getArgs()[0] + " ", "");

		int warnCount = MySQL.loadWarnCount(warned.getId());
		MySQL.insertWarnCount(warned.getId(), warnCount + 1);
		MySQL.insertWarn(warned.getId(), reason);

		event.getChannel().sendMessage(new EmbedBuilder().setColor(Color.orange).setTitle("warn")
				.setDescription(
						warned.getAsMention() + " wurde von " + event.getAuthor().getAsMention() + " verwarndt!")
				.addField("Grund:", "```" + reason + "```", false)
				.setFooter(warned.getName() + " wurde zum " + (warnCount+1) + " mal verwarnt!", null).build())
				.queue();
		
	}



	@Override
	public String help() {
		return null;
	}
}
