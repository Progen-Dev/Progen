package de.progen_bot.listeners.autorole;

import de.progen_bot.database.dao.config.ConfigDaoImpl;
import de.progen_bot.database.entities.config.GuildConfiguration;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class AutoroleListener extends ListenerAdapter
{
    @Override
    public void onGuildMemberJoin(@Nonnull GuildMemberJoinEvent event)
    {
        final GuildConfiguration config = new ConfigDaoImpl().loadConfig(event.getGuild().getIdLong());
        final long roleId = config.getAutorole();

        if (roleId != 0)
        {
            event.getGuild().retrieveMember(event.getUser()).queue(member ->
            {
                final Role role = event.getGuild().getRoleById(roleId);
                if (role != null)
                    // TODO: 22.02.2021 proper error handling with user feedback to owner
                    event.getGuild().addRoleToMember(member, role).reason("Role has been added due to auto role feature by Progen").queue(null, null);
            });
        }
    }
}
