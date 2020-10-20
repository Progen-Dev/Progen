package de.progen_bot.command;

import de.mtorials.misc.Logger;
import de.progen_bot.db.dao.config.ConfigDaoImpl;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.db.entities.config.GuildConfigurationBuilder;
import de.progen_bot.permissions.PermissionCore;
import de.progen_bot.util.MessageGenerator;
import de.progen_bot.util.Settings;
import de.progen_bot.util.Statics;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.*;
import java.util.regex.Pattern;

/**
 * The Class CommandManager.
 */
public class CommandManager extends ListenerAdapter {

    /**
     * The Constant commandAssociations.
     */
    private static final Map<String, CommandHandler> commandAssociations = new HashMap<>();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot() || !event.getChannelType().isGuild()) {
            return;
        }

        // Universal info command
        if (event.getMessage().getContentRaw().equals(Settings.PREFIX + Statics.UNIVERSAL_INFO_CMD_WITHOUT_PREFIX)) {
            CommandHandler commandHandler = commandAssociations.get(Statics.UNIVERSAL_INFO_CMD_WITHOUT_PREFIX);
            Logger.info("Universal info command was invoked by " + event.getGuild().getName());
            ParsedCommandString parsedMessage = parse(event.getMessage().getContentRaw(), Settings.PREFIX);
            commandHandler.execute(parsedMessage, event, new GuildConfigurationBuilder()
                    .setLogChannelID(null)
                    .setPrefix("pb!")
                    .setTempChannelCategoryID(null)
                    .build());
            return;
        }

        GuildConfiguration guildConfiguration = new ConfigDaoImpl().loadConfig(event.getGuild());

        if (guildConfiguration == null) {
            guildConfiguration = new GuildConfigurationBuilder()
                    .setLogChannelID(null)
                    .setPrefix("pb!")
                    .setTempChannelCategoryID(null)
                    .build();

            new ConfigDaoImpl().writeConfig(guildConfiguration, event.getGuild());
                }
            ParsedCommandString parsedMessage = parse(event.getMessage().getContentRaw(), guildConfiguration.getPrefix());

        if (parsedMessage == null) return;

        CommandHandler commandHandler = commandAssociations.get(parsedMessage.getCommand());

        if (commandHandler == null) {
            event.getTextChannel().sendMessage(new MessageGenerator("", "").generateErrorMsg("This is not a command. Use the help command for help.")).queue();
            return;
        }

        Logger.info("Command " + commandHandler.getInvokeString() + " was invoked by " + event.getGuild().getName());

        if (commandHandler.getAccessLevel().getLevel() > new PermissionCore(event).getAccessLevel().getLevel()) {
            event.getTextChannel().sendMessage(new MessageGenerator("", "").generateErrorMsg("Your are not allowed to use this command!")).queue();
            return;
        }
        commandHandler.execute(parsedMessage, event, guildConfiguration);
        event.getMessage().delete().queue();
    }

    /**
     * Sets the up de.progen_bot.command handlers.
     *
     * @param commandHandler the new up de.progen_bot.command handlers
     */
    public void setupCommandHandlers(CommandHandler commandHandler) {
        if (!commandAssociations.containsKey(commandHandler.getInvokeString().toLowerCase())) {
            commandAssociations.put(commandHandler.getInvokeString(), commandHandler);
        }

    }

    /**
     * Gets the de.progen_bot.command handler.
     *
     * @param invocationAlias the invocation alias
     * @return the de.progen_bot.command handler
     */
    public CommandHandler getCommandHandler(String invocationAlias) {
        return commandAssociations.get(invocationAlias.toLowerCase());
    }

    public Map<String, CommandHandler> getCommandAssociations() {
        return commandAssociations;
    }

    /**
     * Parses the.
     *
     * @param message the message
     * @return the parsed de.progen_bot.command string
     */
    private ParsedCommandString parse(String message, String prefix) {
        if (message.startsWith(prefix)) {
            String beheaded = message.replaceFirst(Pattern.quote(prefix), "");
            String[] args = beheaded.split("\\s+");
            String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
            return new ParsedCommandString(args[0], newArgs);
        }
        return null;
    }

    /**
     * The Class ParsedCommandString.
     */
    public static final class ParsedCommandString {

        /**
         * The de.progen_bot.command.
         */
        private final String command;

        /**
         * The args.
         */
        private final String[] args;

        /**
         * Instantiates a new parsed de.progen_bot.command string.
         *
         * @param command the de.progen_bot.command
         * @param args    the args
         */
        public ParsedCommandString(String command, String[] args) {
            this.command = command;
            this.args = args;
        }

        /**
         * Gets the de.progen_bot.command.
         *
         * @return the de.progen_bot.command
         */
        public String getCommand() {
            return command;
        }

        /**
         * Gets the args.
         *
         * @return the args
         */
        public String[] getArgs() {
            return args;
        }

        public List<String> getArgsAsList() {

            return new ArrayList<>(Arrays.asList(args));
        }
    }
}
