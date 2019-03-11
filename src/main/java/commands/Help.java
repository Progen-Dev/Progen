package commands;

import java.awt.Color;
import java.util.Collection;

import command.CommandHandler;
import command.CommandManager.ParsedCommandString;
import core.Main;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import util.Settings;

public class Help extends CommandHandler {

	public Help() {
		super("help", "help", "get some help");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(ParsedCommandString parsedCommand, MessageReceivedEvent event) {
		EmbedBuilder builder = new EmbedBuilder();
		if (parsedCommand.getArgs().length == 0) {
			StringBuilder commandList = new StringBuilder();
			Collection<CommandHandler> commands = Main.getCommandManager().getCommandassociations().values();
			for (CommandHandler commandHandler : commands) {
				commandList.append("`" + commandHandler.getInvokeString() + "` ");
			}

			builder.addField("General", commandList.toString(), true);

			builder.setColor(Color.GREEN);
			builder.setTitle(":information_source: " + event.getJDA().getSelfUser().getName() + " command list");
			builder.setDescription(
					"Use `" + Settings.PREFIX + "help <command>` to get more information about a command.");
			builder.setFooter("Loaded a total of " + Main.getCommandManager().getCommandassociations().values().size()
					+ " commands.", null);

		} else {
			CommandHandler handler = Main.getCommandManager().getCommandHandler(parsedCommand.getArgs()[0]);
			if (handler == null) {
				builder.setColor(Color.red);
				builder.setTitle(":warning: Invalid command");
				builder.setDescription("There is no command named `" + parsedCommand.getArgs()[0] + "`. Use `"
						+ Settings.PREFIX + parsedCommand.getCommand() + "` to get a full command list.");
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
