package de.progen_bot.core.command;

import de.progen_bot.core.Main;
import de.progen_bot.database.DaoHandler;
import de.progen_bot.database.entities.config.GuildConfiguration;
import de.progen_bot.utils.message.MessageGenerator;
import de.progen_bot.utils.permission.AccessLevel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public abstract class CommandHandler
{
    private final MessageGenerator generator;
    private final String invoke;
    private final String usage;
    private final String description;
    private final DaoHandler handler = Main.getDaoHandler();

    public CommandHandler(String invoke, String usage, String description)
    {
        this.invoke = invoke;
        this.usage = usage;
        this.description = description;
        this.generator = new MessageGenerator(this.usage, this.invoke);
    }

    public abstract void execute(CommandManager.ParsedCommandString commandString, GuildMessageReceivedEvent event, GuildConfiguration configuration);

    public String getInvoke()
    {
        return invoke;
    }

    public String getUsage()
    {
        return usage;
    }

    public String getDescription()
    {
        return description;
    }

    public DaoHandler getHandler()
    {
        return handler;
    }

    public MessageGenerator getGenerator()
    {
        return generator;
    }

    // TODO: 22.02.2021 maybe interface and default value?
    public abstract AccessLevel getAccessLevel();
}
