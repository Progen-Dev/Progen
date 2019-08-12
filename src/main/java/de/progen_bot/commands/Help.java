package de.progen_bot.commands;

import java.awt.Color;
import java.util.Collection;

import de.mtorials.config.GuildConfiguration;
import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager.ParsedCommandString;
import de.progen_bot.core.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import de.progen_bot.util.Settings;

public class Help extends CommandHandler {

	public Help() {
		super("help", "help", "get some help");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {
		EmbedBuilder builder = new EmbedBuilder();
		if (parsedCommand.getArgs().length == 0) {
			StringBuilder commandList = new StringBuilder();
			Collection<CommandHandler> commands = Main.getCommandManager().getCommandassociations().values();
			for (CommandHandler commandHandler : commands) {
				commandList.append("`" + commandHandler.getInvokeString() + "` ");
			}

			builder.addField("General", commandList.toString(), true);

			builder.setColor(Color.GREEN);
			builder.setTitle(":information_source: " + event.getJDA().getSelfUser().getName() + " de.progen_bot.command list");
			builder.setDescription(
					"Use `" + Settings.PREFIX + "help <de.progen_bot.command>` to get more information about a de.progen_bot.command.");
			builder.setFooter("Loaded a total of " + Main.getCommandManager().getCommandassociations().values().size()
					+ " de.progen_bot.commands.", null);

		} else {
			CommandHandler handler = Main.getCommandManager().getCommandHandler(parsedCommand.getArgs()[0]);
			if (handler == null) {
				builder.setColor(Color.red);
				builder.setTitle(":warning: Invalid de.progen_bot.command");
				builder.setDescription("There is no de.progen_bot.command named `" + parsedCommand.getArgs()[0] + "`. Use `"
						+ Settings.PREFIX + parsedCommand.getCommand() + "` to get a full de.progen_bot.command list.");
			} else {
				builder.setColor(Color.green);
				builder.setTitle("Command Infos");
				builder.setDescription(handler.getDescription());
				builder.addField("Commands:", "`" + handler.getCommandUsage() + "`", true);
			}
		}
		event.getChannel().sendMessage(builder.build()).queue();
	}

	@Override
	public String help() {
		return null;
	}
}
