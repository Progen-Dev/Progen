package de.progen_bot.commands.xp;

import de.progen_bot.core.command.CommandHandler;
import de.progen_bot.core.command.CommandManager;
import de.progen_bot.database.entities.config.GuildConfiguration;
import de.progen_bot.utils.permission.AccessLevel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class XPNotification extends CommandHandler
{
    private static final XPNotify NOTIFY = new XPNotify();

    public XPNotification()
    {
        super("xpnotification", "xpnotification", NOTIFY.getDescription());
    }

    @Override
    public void execute(CommandManager.ParsedCommandString commandString, GuildMessageReceivedEvent event, GuildConfiguration configuration)
    {
        NOTIFY.execute(commandString, event, configuration);
    }

    @Override
    public AccessLevel getAccessLevel()
    {
        return NOTIFY.getAccessLevel();
    }
}
