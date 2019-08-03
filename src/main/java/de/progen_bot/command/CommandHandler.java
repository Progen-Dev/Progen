package de.progen_bot.command;

import de.mtorials.config.GuildConfiguration;
import de.mtorials.db.DAOHandler;
import de.progen_bot.core.Main;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

/**
 * The Class CommandHandler.
 */
public abstract class CommandHandler {

	/**
	 * The error.
	 */
	public MessageEmbed generateErrorMsg(String error) {

		return new EmbedBuilder()
				.setColor(Color.RED)
				.setDescription(":no_entry: " + error + ":no_entry:")
				.build();
	}

	public MessageEmbed generateErrorMsgWrongInput() {

		return new EmbedBuilder()
				.setColor(Color.RED)
				.setTitle("ERROR")
				.addField("Use the command like this:", this.commandUsage, false)
				.build();
	}

	public MessageEmbed generateSuccessfulMsg() {

		return new EmbedBuilder()
				.setColor(Color.BLUE)
				.setTitle("SUCCESSFUL")
				.setDescription("Successfully executed command " + this.invokeString + ".")
				.build();
	}

	/**
	 * The warning
	 */
	public  MessageEmbed generateWarningMsg (String warning){
		return new EmbedBuilder()
				.setColor(Color.YELLOW)
				.setTitle("WARNING")
				.setDescription(":warning: " + warning + " :warning:")
				.build();
	}

	/**
	 * Right
	 */
	public  MessageEmbed generateRightMsg(String right){
		return new EmbedBuilder()
				.setColor(Color.GREEN)
				.setTitle("RIGHT")
				.setDescription("" + right + " :white_check_mark:")
				.build();
	}

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
	 * @param parsedCommand the parsed de.progen_bot.command
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
