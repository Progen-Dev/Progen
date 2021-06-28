package de.progen_bot.commands.fun;

import de.progen_bot.core.command.CommandHandler;
import de.progen_bot.core.command.CommandManager;
import de.progen_bot.database.entities.config.GuildConfiguration;
import de.progen_bot.utils.permission.AccessLevel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class PingPong extends CommandHandler
{
    public PingPong()
    {
        super("ping", "pingpong", "pingpong the game");
    }

    @Override
    public void execute(CommandManager.ParsedCommandString commandString, GuildMessageReceivedEvent event, GuildConfiguration configuration)
    {
        event.getChannel().sendMessage(":ping_pong: Pong!").queue();
    }

    @Override
    public AccessLevel getAccessLevel()
    {
        return AccessLevel.USER;
    }
}
