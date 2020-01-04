package de.progen_bot.commands.moderator.Blacklist;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.core.PermissionCore;
import de.progen_bot.db.entities.config.GuildConfiguration;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandBan extends CommandHandler {
    public CommandBan() {
        super("ban" , "ban <user> <reason>" , "Ban a user from this Server");
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand , MessageReceivedEvent event , GuildConfiguration configuration) {
        if (PermissionCore.check(2 , event)) ;


    }

    @Override
    public String help() {
        return null;
    }
}
