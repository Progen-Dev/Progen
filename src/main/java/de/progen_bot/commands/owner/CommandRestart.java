package de.progen_bot.commands.owner;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.permissions.AccessLevel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.IOException;

public class CommandRestart extends CommandHandler {

    private static final String OS_NAME = System.getProperty("os.name");

    public CommandRestart() {
        super("restart", "restart", "Restart Progen. Only for Owner!");
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {
        ProcessBuilder pb;

        if (OS_NAME.startsWith("Windows"))
            pb = new ProcessBuilder("cmd.exe", "/c", "python restart.py");
        else
            pb = new ProcessBuilder("/usr/bin/env", "bash", "-c", "python restart.py");

        try {
            pb.redirectOutput(ProcessBuilder.Redirect.INHERIT).start();
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String help() {
        return null;
    }

    @Override
    public AccessLevel getAccessLevel() {
        return AccessLevel.BOTOWNER;
    }
}
