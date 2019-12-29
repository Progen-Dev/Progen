package de.progen_bot.commands.etc;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.db.entities.config.GuildConfiguration;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Test extends CommandHandler {
    public Test() {
        super("test", "test", "test");
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand , MessageReceivedEvent event , GuildConfiguration configuration) {

    }

    @Override
    public String help() {
        return null;
    }
}