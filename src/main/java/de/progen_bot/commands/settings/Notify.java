package de.progen_bot.commands.settings;

import de.progen_bot.core.command.CommandHandler;
import de.progen_bot.core.command.CommandManager;
import de.progen_bot.database.entities.config.GuildConfiguration;
import de.progen_bot.utils.permission.AccessLevel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Notify extends CommandHandler
{
    public Notify()
    {
        super("notify", "notify", "Assign a role to yourself and get notified on news. Mainly for Progen, but you can create a role name `Notify` in order to let it work on your guild.");
    }

    @Override
    public void execute(CommandManager.ParsedCommandString commandString, GuildMessageReceivedEvent event, GuildConfiguration configuration)
    {
        final Guild guild = event.getGuild();
        final Member member = event.getMember();
        final Role role;

        if (member == null || guild.getRolesByName("Notify", true).isEmpty())
            return;
        else
            role = guild.getRolesByName("Notify", true).get(0);

        if (role == null)
            return;

        guild.addRoleToMember(member, role).queue();
    }

    @Override
    public AccessLevel getAccessLevel()
    {
        return AccessLevel.USER;
    }
}
