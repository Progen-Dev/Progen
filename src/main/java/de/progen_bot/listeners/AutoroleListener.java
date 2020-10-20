package de.progen_bot.listeners;

import javax.annotation.Nonnull;

import de.progen_bot.db.dao.config.ConfigDaoImpl;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class AutoroleListener extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(@Nonnull GuildMemberJoinEvent event) {
        final String roleId = new ConfigDaoImpl().loadConfig(event.getGuild()).getAutoRole();

        if (roleId != null) {
            event.getGuild().retrieveMember(event.getUser()).queue(member -> {
                final Role role = event.getGuild().getRoleById(roleId);
                if (role != null)
                    event.getGuild().modifyMemberRoles(member, role).reason("Role added due to auto role feature")
                            .queue();
            });
        }
    }
}