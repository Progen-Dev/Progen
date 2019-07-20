/**
 * 
 */
package de.progen_bot.commands.xp;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager.ParsedCommandString;
import de.progen_bot.db.UserData;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * The Class XPNotify.
 */
public class XPNotify extends CommandHandler {

	/**
	 * Instantiates a new XP notify.
	 */
	public XPNotify() {
		super("xpnotify","xpnotify","disable or enable the lvl up notification");
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
		UserData data = UserData.fromId(event.getAuthor().getId());

		if (data.getLvlupNotify()) {
			data.setLvlupNotify(false);
			event.getTextChannel().sendMessage("LevelUp message successful disabled").queue();
		} else {
			data.setLvlupNotify(true);
			event.getTextChannel().sendMessage("LevelUp message successful enabled").queue();
		}
		data.save(data);
	}

	@Override
	public String help() {
		return null;
	}
}
