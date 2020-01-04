package de.progen_bot.commands.Owner;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.core.PermissionCore;
import de.progen_bot.db.entities.config.GuildConfiguration;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Timer;
import java.util.TimerTask;

public class CommandRestart extends CommandHandler {
    public CommandRestart() {
        super("restart", "restart", "Restart Progen. Only for Owner!");
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {
        if (PermissionCore.check(4 , event)) return;
    }

    @Override
    public String help() {
        return null;
    }
}