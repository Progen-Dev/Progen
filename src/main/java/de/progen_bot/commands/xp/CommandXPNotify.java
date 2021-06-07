package de.progen_bot.commands.xp;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager.ParsedCommandString;
import de.progen_bot.db.entities.UserData;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.permissions.AccessLevel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;


/**
 * The Class XPNotify.
 */
public class CommandXPNotify extends CommandHandler {

    /**
     * Instantiates a new XP notify.
     */
    public CommandXPNotify() {
        super("xpnotify", "xpnotify", "enable or disable the lvl up notification");
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.progen_bot.command.CommandHandler#execute(de.progen_bot.command.CommandManager.ParsedCommandString,
     * net.dv8tion.jda.de.progen_bot.core.events.message.MessageReceivedEvent)
     */
    @Override
    public void execute(ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {
        UserData data = UserData.fromId(event.getAuthor().getId());

        if (data.getLvlUpNotify()) {
            data.setLvlUpNotify(false);
            event.getTextChannel().sendMessage("LevelUp message successfully disabled").queue();
        } else {
            data.setLvlUpNotify(true);
            event.getTextChannel().sendMessage("LevelUp message successfully enabled").queue();
        }
        data.save(data);
    }

    @Override
    public AccessLevel getAccessLevel() {
        return AccessLevel.USER;
    }
}
