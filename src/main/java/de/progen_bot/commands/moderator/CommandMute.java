package de.progen_bot.commands.moderator;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.permissions.AccessLevel;
import de.progen_bot.permissions.PermissionCore;
import de.progen_bot.db.entities.config.GuildConfiguration;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandMute extends CommandHandler {

    public CommandMute() {
        super("mute", "mute <user>", "mute a user");
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration guildConfiguration) {


    }

    @Override
    public String help() {
        return null;
    }

    @Override
    public AccessLevel getAccessLevel() {
        return AccessLevel.MODERATOR;
    }
}
