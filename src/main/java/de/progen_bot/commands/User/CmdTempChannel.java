package de.progen_bot.commands.User;

import de.mtorials.config.GuildConfiguration;
import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.core.TempChannelController;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;

public class CmdTempChannel extends CommandHandler {

    public CmdTempChannel() {
        super("tempchannel", "tempchannel create", "Creates your private temporary channel");
    }

    private void createNewTempChannel(ArrayList<String> args, MessageReceivedEvent event, GuildConfiguration configuration) {

        TempChannelController tempChannelController = new TempChannelController(event.getGuild().getCategoryById(configuration.tempChannelCatergoryID));

        if (tempChannelController.getTempChannelCount() >= 3) tempChannelController.removeOldestTempChannel();
        if (args.size() >= 3) tempChannelController.createTextChannel(args.get(1), Integer.getInteger(args.get(3)));
        else tempChannelController.createTextChannel(args.get(1));

        event.getTextChannel().sendMessage("Temp channel successfully created!").queue();
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {

        switch (parsedCommand.getArgsAsList().get(0).toLowerCase()) {

            case "create":

                createNewTempChannel(parsedCommand.getArgsAsList(), event, configuration);
        }
    }

    @Override
    public String help() {
        return null;
    }
}
