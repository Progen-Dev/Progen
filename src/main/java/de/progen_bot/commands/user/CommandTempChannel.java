package de.progen_bot.commands.user;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.core.TempChannelController;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.permissions.AccessLevel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;

public class CommandTempChannel extends CommandHandler {

    public CommandTempChannel() {
        super("tempchannel", "tempchannel create", "Creates your private temporary channel");
    }

    private void createNewTempChannel(ArrayList<String> args, MessageReceivedEvent event, GuildConfiguration configuration) {

        TempChannelController tempChannelController = new TempChannelController(event.getGuild().getCategoryById(configuration.getTempChannelCategoryID()));

        if (tempChannelController.getTempChannelCount() >= 3) tempChannelController.removeOldestTempChannel();
        if (args.size() >= 3) tempChannelController.createTextChannel(args.get(1), Integer.getInteger(args.get(3)));
        else tempChannelController.createTextChannel(args.get(1));

        event.getTextChannel().sendMessage("Temp channel successfully created!").queue();
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {

        if ("create".equalsIgnoreCase(parsedCommand.getArgsAsList().get(0))) {
            createNewTempChannel((ArrayList<String>) parsedCommand.getArgsAsList(), event, configuration);
        }
    }

    @Override
    public AccessLevel getAccessLevel() {
        return AccessLevel.USER;
    }
}
