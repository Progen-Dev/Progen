package de.progen_bot.commands.Settings;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.db.entities.config.GuildConfiguration;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandNotify extends CommandHandler {
    public CommandNotify() {super("notify", "get role notify", "description");
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {
        event.getGuild().addRoleToMember(event.getMember(), (Role) event.getJDA().getRolesByName("Notify", true)).complete();

    }

    @Override
    public String help() {
        return null;
    }
}
