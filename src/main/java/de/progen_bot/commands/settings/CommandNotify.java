package de.progen_bot.commands.settings;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.permissions.AccessLevel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandNotify extends CommandHandler {
    public CommandNotify() {super("notify", "notify", "Returns the role Notify. This is a kind of newsletter with which you can be pinned for news.");
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {

        final Guild guild = event.getGuild();
        final Role notify = guild.getRolesByName("Notify", true).get(0);
        final Member member = event.getMember();

        if (member == null)
            return;

        event.getGuild().addRoleToMember(member, notify).queue();
    }

    @Override
    public String help() {
        return null;
    }

    @Override
    public AccessLevel getAccessLevel() {
        return AccessLevel.USER;
    }
}
