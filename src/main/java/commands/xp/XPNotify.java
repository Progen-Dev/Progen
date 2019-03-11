/**
 * 
 */
package commands.xp;

import command.CommandHandler;
import command.CommandManager.ParsedCommandString;
import db.UserData;
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
	 * command.CommandHandler#execute(command.CommandManager.ParsedCommandString,
	 * net.dv8tion.jda.core.events.message.MessageReceivedEvent)
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
