package de.progen_bot.commands.Moderator;

import de.mtorials.config.GuildConfiguration;
import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager.ParsedCommandString;
import de.progen_bot.core.PermissionCore;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;


public class Warn extends CommandHandler {

	public Warn() {
		super("warn", "warn <user> <reason>", "warned a user");
	}

	@Override
	public void execute(ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {
		if (PermissionCore.check(3, event)) return;

		Member warned;
		if (event.getMessage().getMentionedUsers().size() == 1) {
			warned = event.getMessage().getMentionedMembers().get(0);
		} else {
			event.getChannel().sendMessage(new EmbedBuilder().setColor(Color.red).setDescription("No user found").build())
					.queue();
			return;
		}

		if (parsedCommand.getArgs().length <= 1) {
			event.getChannel().sendMessage(new EmbedBuilder().setColor(Color.red).setDescription("No reason found").build())
					.queue();
			return;
		}

		String reason = String.join(" ", parsedCommand.getArgs()).replace(parsedCommand.getArgs()[0] + " ", "");

		int warnCount = super.getDAOs().getWarnList().getWarnsByMember(warned).size();
		super.getDAOs().getWarnList().addWarnForMember(warned, reason);

		event.getChannel().sendMessage(new EmbedBuilder().setColor(Color.orange).setTitle("warn")
				.setDescription(
						warned.getAsMention() + " wurde von " + event.getAuthor().getAsMention() + " verwarnt!")
				.addField("Grund:", "```" + reason + "```", false)
				.setFooter(warned.getEffectiveName() + " wurde zum " + (warnCount + 1) + " mal verwarnt!", null).build())
				.queue();

	}


	@Override
	public String help() {
		return null;
	}
}
