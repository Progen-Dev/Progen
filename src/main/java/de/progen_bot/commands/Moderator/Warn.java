package de.progen_bot.commands.Moderator;

import java.awt.Color;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager.ParsedCommandString;
import de.progen_bot.core.PermissionCore;
import de.progen_bot.db.MySQL;
import net.dv8tion.jda.core.EmbedBuilder;
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

		if(parsedCommand.getArgs().length <= 1){
			event.getChannel().sendMessage(new EmbedBuilder().setColor(Color.red).setDescription("No reason found").build())
					.queue();
			return;
		}

		String reason = String.join(" ", parsedCommand.getArgs()).replace(parsedCommand.getArgs()[0] + " ", "");

		int warnCount = MySQL.loadWarnCount(warned.getId());
		MySQL.insertWarnCount(warned.getId(), warnCount + 1);
		MySQL.insertWarn(warned.getId(), reason);

		event.getChannel().sendMessage(new EmbedBuilder().setColor(Color.orange).setTitle("warn")
				.setDescription(
						warned.getAsMention() + " wurde von " + event.getAuthor().getAsMention() + " verwarnt!")
				.addField("Grund:", "```" + reason + "```", false)
				.setFooter(warned.getName() + " wurde zum " + (warnCount+1) + " mal verwarnt!", null).build())
				.queue();
		
	}



	@Override
	public String help() {
		return null;
	}
}
