package de.progen_bot.core.command;

import de.progen_bot.database.dao.config.ConfigDaoImpl;
import de.progen_bot.database.entities.config.GuildConfiguration;
import de.progen_bot.utils.logger.Logger;
import de.progen_bot.utils.message.MessageGenerator;
import de.progen_bot.utils.permission.AccessLevel;
import de.progen_bot.utils.statics.Settings;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

// TODO: 22.02.2021 implement aliases
public class CommandManager extends ListenerAdapter
{
    private final Map<String, CommandHandler> commands = new HashMap<>();

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event)
    {
        if (event.getAuthor().isBot() || event.getAuthor().getIdLong() == event.getJDA().getSelfUser().getIdLong() || !event.getMessage().getContentRaw().startsWith(Settings.PREFIX))
            return;

        final GuildConfiguration configuration = new ConfigDaoImpl().loadConfig(event.getGuild().getIdLong());

        if (event.getMessage().getContentRaw().equalsIgnoreCase(Settings.PREFIX + "info"))
        {
            final CommandHandler handler = this.commands.get("info");
            final ParsedCommandString commandString = parse(event.getMessage().getContentRaw(), Settings.PREFIX);

            handler.execute(commandString, event, configuration);

            Logger.debug("Universal info command has been invoked by " + event.getAuthor().getAsTag() + " on guild" + String.format("%s (%s)", event.getGuild().getName(), event.getGuild().getId()));

            return;
        }

        final ParsedCommandString commandString = parse(event.getMessage().getContentRaw(), configuration.getPrefix());
        if (commandString == null)
            return;

        final CommandHandler handler = this.getCommandHandler(commandString.getCommand().toLowerCase());
        if (handler == null)
        {
            event.getChannel().sendMessage(new MessageGenerator("", "").generateErrorMsg("This command is not registered. Use `" + configuration.getPrefix() + "help` for help.")).queue();
            return;
        }

        Logger.debug("Command '" + handler.getInvoke() + "' has been invoked by " + event.getAuthor().getAsTag() + " on guild" + String.format("%s (%s)", event.getGuild().getName(), event.getGuild().getId()));

        if (AccessLevel.isAllowed(handler.getAccessLevel().getLevel(), event))
        {
            handler.execute(commandString, event, configuration);
            event.getMessage().delete().queue(null, RestAction.getDefaultFailure());
        }
    }

    private static ParsedCommandString parse(String content, String prefix)
    {
        if (content.startsWith(prefix))
        {
            final String beheaded = content.replaceFirst(Pattern.quote(prefix), "");
            final String[] args = beheaded.split("\\s+");

            return new ParsedCommandString(args[0], Arrays.copyOfRange(args, 1, args.length));
        }

        return null;
    }

    public void setupCommandHandler(CommandHandler handler)
    {
        if (!this.commands.containsKey(handler.getInvoke().toLowerCase()))
            this.commands.put(handler.getInvoke().toLowerCase(), handler);
    }

    public CommandHandler getCommandHandler(String invoke)
    {
        invoke = invoke.toLowerCase();

        if (this.commands.containsKey(invoke.toLowerCase()))
            return this.commands.get(invoke.toLowerCase());

        return null;
    }

    public Map<String, CommandHandler> getCommands()
    {
        return this.commands;
    }

    public static final class ParsedCommandString
    {
        private final String command;
        private final String[] args;

        public ParsedCommandString(String command, String[] args)
        {
            this.command = command;
            this.args = args;
        }

        public String getCommand()
        {
            return command;
        }

        public String[] getArgs()
        {
            return args;
        }

        public List<String> getArgsList()
        {
            return Arrays.asList(this.args);
        }
    }
}
