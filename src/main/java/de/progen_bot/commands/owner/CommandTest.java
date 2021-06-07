package de.progen_bot.commands.owner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager.ParsedCommandString;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.permissions.AccessLevel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandTest extends CommandHandler {
    public CommandTest() {
        super("test" , "test" , "test");
    }

    @Override
    public void execute(ParsedCommandString parsedCommand , MessageReceivedEvent event , GuildConfiguration configuration) {
    }

    @Override
    public AccessLevel getAccessLevel() {
        return AccessLevel.OWNER;
    }

}