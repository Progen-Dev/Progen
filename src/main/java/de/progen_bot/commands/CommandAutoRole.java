package de.progen_bot.commands;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class CommandAutoRole extends CommandHandler {
    public CommandAutoRole(){super("","","");}


    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand , MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return null;
    }
}
