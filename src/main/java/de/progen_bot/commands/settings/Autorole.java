package de.progen_bot.commands.settings;

import de.progen_bot.core.command.CommandHandler;
import de.progen_bot.core.command.CommandManager;
import de.progen_bot.database.dao.config.ConfigDaoImpl;
import de.progen_bot.database.entities.config.GuildConfiguration;
import de.progen_bot.utils.permission.AccessLevel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Autorole extends CommandHandler
{
    public Autorole()
    {
        super("autorole", "Activate: `autorole <@role>` or `autorole <roleid>`\nDeactivate: `autorole`", "Set a role users will get when they join.");
    }

    @Override
    public void execute(CommandManager.ParsedCommandString commandString, GuildMessageReceivedEvent event, GuildConfiguration configuration)
    {
        if (!event.getMessage().getMentionedRoles().isEmpty())
        {
            final Role role = event.getMessage().getMentionedRoles().get(0);

            configuration.setAutoRole(role.getIdLong());
            event.getChannel().sendMessage(this.getGenerator().generateSuccessMsg("Successfully set autorole to `" + role.getName() + " (" + role.getId() + ")`.")).queue();
        }
        else if (commandString.getArgs().length == 1)
        {
            long id;
            try
            {
                id = Long.parseLong(commandString.getArgs()[0]);
            }
            catch (NumberFormatException e)
            {
                id = 0;
            }

            if (id == 0)
            {
                event.getChannel().sendMessage(this.getGenerator().generateErrorMsg("Please provide a role id. To disable autorole run `autorole` with no arguments.")).queue();
                return;
            }

            final Role role = event.getGuild().getRoleById(id);
            if (role == null)
            {
                event.getChannel().sendMessage(this.getGenerator().generateErrorMsg("Invalid role id.")).queue();
                return;
            }

            configuration.setAutoRole(role.getIdLong());
            event.getChannel().sendMessage(this.getGenerator().generateSuccessMsg("Successfully set autorole to `" + role.getName() + " (" + role.getId() + ")`.")).queue();
        }
        else
        {
            configuration.setAutoRole(0);
            event.getChannel().sendMessage(this.getGenerator().generateSuccessMsg("Successfully deactivated autorole.")).queue();
        }

        new ConfigDaoImpl().writeConfig(configuration, event.getGuild().getIdLong());
    }

    @Override
    public AccessLevel getAccessLevel()
    {
        return AccessLevel.MODERATOR;
    }
}
