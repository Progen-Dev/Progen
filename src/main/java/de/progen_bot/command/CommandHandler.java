package de.progen_bot.command;

import de.mtorials.config.GuildConfiguration;
import de.mtorials.db.DAOHandler;
import de.progen_bot.core.Main;
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
	 * The de.progen_bot.command usage.
	 */
	private String commandUsage;

	/**
	 * The description.
	 */
	private String description;

	/**
	 * The DAO handling class
	 */
	private DAOHandler daoHandler = Main.getDAOs();

	/**
	 * Instantiates a new de.progen_bot.command handler.
	 *
	 * @param invokeString the invoke string
	 * @param commandUsage the de.progen_bot.command usage
	 * @param description  the description
	 */
	public CommandHandler(String invokeString , String commandUsage , String description) {
		this.invokeString = invokeString;
		this.commandUsage = commandUsage;
		this.description = description;
	}

	/**
	 * Execute.
	 *  @param parsedCommand the parsed de.progen_bot.command
	 * @param event         the event
	 * @param configuration
	 */
	public abstract void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration);

	/**
	 * Gets the invoke string.
	 *
	 * @return the invoke string
	 */
	public String getInvokeString() {
		return invokeString;
	}

	/**
	 * Gets the de.progen_bot.command usage.
	 *
	 * @return the de.progen_bot.command usage
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

	public DAOHandler getDAOs() {
		return daoHandler;
	}

	public abstract String help();
}
