package de.progen_bot.listeners;

import javax.annotation.Nonnull;

import de.progen_bot.db.dao.config.ConfigDaoImpl;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.db.entities.config.GuildConfigurationBuilder;
import de.progen_bot.util.Settings;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class AutoroleListener extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(@Nonnull GuildMemberJoinEvent event) {
        final ConfigDaoImpl config = new ConfigDaoImpl();

        GuildConfiguration guildConfiguration = config.loadConfig(event.getGuild());
        //check if guild is already registered in database
        if (guildConfiguration == null) {
            guildConfiguration = new GuildConfigurationBuilder().setLogChannelID(null).setPrefix(Settings.PREFIX)
                    .setTempChannelCategoryID(null).setAutorole(null).build();

            config.writeConfig(guildConfiguration, event.getGuild());

        }
        String roleId = guildConfiguration.getAutoRole();

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