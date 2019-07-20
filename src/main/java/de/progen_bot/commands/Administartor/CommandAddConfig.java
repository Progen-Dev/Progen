package de.progen_bot.commands.Administartor;

import de.mtorials.config.GuildConfiguration;
import de.mtorials.config.GuildConfigurationBuilder;
import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.core.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;

public class CommandAddConfig extends CommandHandler {

    public CommandAddConfig() {
        super("configurate", "debug", "Moves to webinterface");
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {

        ArrayList<String> params = parsedCommand.getArgsAsList();

        GuildConfiguration guildConfiguration = new GuildConfigurationBuilder()
                .setLogChannelID(params.get(0))
                .setPrefix(params.get(1))
                .setTempChannelCatergoryID(params.get(2))
                .build();

        Main.getConfiguration().writeGuildConfiguration(event.getGuild(), guildConfiguration);
        System.out.println("Wrote config");
    }

    @Override
    public String help() {
        return null;
    }
}
