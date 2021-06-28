package de.progen_bot.commands.owner;

import de.progen_bot.core.command.CommandHandler;
import de.progen_bot.core.command.CommandManager;
import de.progen_bot.database.entities.config.GuildConfiguration;
import de.progen_bot.utils.permission.AccessLevel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class CommandLog extends CommandHandler
{
    public CommandLog()
    {
        super("log", "log", "Logbook for the owners of Progen.");
    }

    @Override
    public void execute(CommandManager.ParsedCommandString commandString, GuildMessageReceivedEvent event, GuildConfiguration configuration)
    {

    }

    @Override
    public AccessLevel getAccessLevel()
    {
        return AccessLevel.BOT_OWNER;
    }
}
