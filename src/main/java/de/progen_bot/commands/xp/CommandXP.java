package de.progen_bot.commands.xp;

import java.awt.Color;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager.ParsedCommandString;
import de.progen_bot.db.UserData;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import de.progen_bot.util.Level;

/**
 * The Class XP.
 */
public class CommandXP extends CommandHandler {

	/**
	 * Instantiates a new xp.
	 */
	public CommandXP() {
		super("xp", "xp <username>", "Get the xp");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.progen_bot.command.CommandHandler#execute(de.progen_bot.command.CommandManager.ParsedCommandString,
	 * net.dv8tion.jda.de.progen_bot.core.events.message.MessageReceivedEvent)
	 */
	@Override
	public void execute(ParsedCommandString parsedCommand, MessageReceivedEvent event) {
		String id = "";
		if (parsedCommand.getArgs().length == 0) {
			id = event.getAuthor().getId();
		} else if (event.getMessage().getMentionedMembers().size() > 0) {
			id = event.getMessage().getMentionedMembers().get(0).getUser().getId();
		} else if (event.getGuild().getMembersByEffectiveName(parsedCommand.getArgs()[0], true).size() > 0) {
			id = event.getGuild().getMembersByEffectiveName(parsedCommand.getArgs()[0], true).get(0).getUser().getId();
		} else {
			return;
		}

		UserData data = UserData.fromId(id);

		EmbedBuilder eb = new EmbedBuilder().setColor(Color.green).setFooter(
				event.getGuild().getMemberById(id).getUser().getName() + "#"
						+ event.getGuild().getMemberById(id).getUser().getDiscriminator(),
				event.getGuild().getMemberById(id).getUser().getAvatarUrl());

		if (data != null) {
			double percent = 100 - Double.valueOf(Level.remainingXp(data.getTotalXp()))
					/ Double.valueOf(Level.xpToLevelUp(data.getLevel())) * 100;

			eb.setTitle("Level: " + data.getLevel() + " (" + Level.remainingXp(data.getTotalXp()) + "/"
					+ Level.xpToLevelUp(data.getLevel()) + ")" + "XP")
					.setDescription("Noch " + percent + " % bis zum nächsten Level");
		} else {
			eb.setTitle("Level: 0 (0/0)XP").setDescription("You have not sent a message yet");
		}
		event.getTextChannel().sendMessage(eb.build()).queue();

	}

	@Override
	public String help() {
		return null;
	}


}
