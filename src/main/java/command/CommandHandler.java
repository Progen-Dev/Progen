package command;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

/**
 * The Class CommandHandler.
 */
public abstract class CommandHandler {
	/**
	 * The error.
	 */
	public static EmbedBuilder error = new EmbedBuilder().setColor(Color.HSBtoRGB(85 , 1 , 100));

	/**
	 * The invoke string.
	 */
	private String invokeString;

	/**
	 * The command usage.
	 */
	private String commandUsage;

	/**
	 * The description.
	 */
	private String description;

	/**
	 * Instantiates a new command handler.
	 *
	 * @param invokeString the invoke string
	 * @param commandUsage the command usage
	 * @param description  the description
	 */
	public CommandHandler(String invokeString , String commandUsage , String description) {
		this.invokeString = invokeString;
		this.commandUsage = commandUsage;
		this.description = description;
	}

	/**
	 * Execute.
	 *
	 * @param parsedCommand the parsed command
	 * @param event         the event
	 */
	public abstract void execute(CommandManager.ParsedCommandString parsedCommand , MessageReceivedEvent event);

	/**
	 * Gets the invoke string.
	 *
	 * @return the invoke string
	 */
	public String getInvokeString() {
		return invokeString;
	}

	/**
	 * Gets the command usage.
	 *
	 * @return the command usage
	 */
	public String getCommandUsage() {
		return commandUsage;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	public abstract String help();
}
