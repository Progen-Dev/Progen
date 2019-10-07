package de.progen_bot.commands.etc;

import de.mtorials.config.GuildConfiguration;
import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class test extends CommandHandler{
    public test(){super("test","test","test");}

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand , MessageReceivedEvent event , GuildConfiguration configuration) {

    }

    @Override
    public String help() {
        return null;
    }
}