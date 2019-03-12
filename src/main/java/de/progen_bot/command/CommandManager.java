package de.progen_bot.command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import de.progen_bot.util.Settings;

/**
 * The Class CommandManager.
 */
public class CommandManager extends ListenerAdapter {

	/** The Constant commandAssociations. */
	private final static HashMap<String, CommandHandler> commandAssociations = new HashMap<>();

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		ParsedCommandString parsedMessage = parse(event.getMessage().getContentRaw());

		if (!event.getAuthor().isBot() && !event.getAuthor().isFake() && parsedMessage != null
				&& event.getChannelType().isGuild()) {
			CommandHandler commandHandler = commandAssociations.get(parsedMessage.getCommand());

			if (commandHandler != null) {
				commandHandler.execute(parsedMessage, event);
				return;
			}
		} else {
			return;
		}
	}

	/**
	 * Sets the up de.progen_bot.command handlers.
	 *
	 * @param commandHandler
	 *            the new up de.progen_bot.command handlers
	 */
	public void setupCommandHandlers(CommandHandler commandHandler) {
		if (commandAssociations.containsKey(commandHandler.getInvokeString().toLowerCase())) {
			return;
		} else {
			commandAssociations.put(commandHandler.getInvokeString(), commandHandler);
		}

	}

	/**
	 * Gets the de.progen_bot.command handler.
	 *
	 * @param invocationAlias
	 *            the invocation alias
	 * @return the de.progen_bot.command handler
	 */
	public CommandHandler getCommandHandler(String invocationAlias) {
		return commandAssociations.get(invocationAlias.toLowerCase());
	}

	public HashMap<String, CommandHandler> getCommandassociations() {
		return commandAssociations;
	}

	/**
	 * Parses the.
	 *
	 * @param message
	 *            the message
	 * @return the parsed de.progen_bot.command string
	 */
	private ParsedCommandString parse(String message) {
		if (message.startsWith(Settings.PREFIX)) {
			String beheaded = message.replaceFirst(Pattern.quote(Settings.PREFIX), "");
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

		/** The de.progen_bot.command. */
		private final String command;

		/** The args. */
		private final String[] args;

		/**
		 * Instantiates a new parsed de.progen_bot.command string.
		 *
		 * @param command
		 *            the de.progen_bot.command
		 * @param args
		 *            the args
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
	}
}
