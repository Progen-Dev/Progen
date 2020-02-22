package de.progen_bot.commands.owner;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.permissions.AccessLevel;
import de.progen_bot.permissions.PermissionCore;
import de.progen_bot.db.entities.config.GuildConfiguration;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandRestart extends CommandHandler {
    public CommandRestart() {
        super("restart", "restart", "Restart Progen. Only for Owner!");
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {
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
