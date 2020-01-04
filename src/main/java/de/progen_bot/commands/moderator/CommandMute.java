package de.progen_bot.commands.moderator;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.core.PermissionCore;
import de.progen_bot.db.entities.config.GuildConfiguration;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.HashMap;

public class CommandMute extends CommandHandler {

    public CommandMute() {
        super("mute", "mute <user>", "mute a user");
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration guildConfiguration) {

        if (PermissionCore.check(1, event)) return;


    }

    @Override
    public String help() {
        return null;
    }
}
