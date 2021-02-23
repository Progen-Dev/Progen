package de.progen_bot.commands.settings;

import de.progen_bot.core.command.CommandHandler;
import de.progen_bot.core.command.CommandManager;
import de.progen_bot.database.dao.config.ConfigDaoImpl;
import de.progen_bot.database.entities.config.GuildConfiguration;
import de.progen_bot.utils.permission.AccessLevel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class ChangePrefix extends CommandHandler
{
    public ChangePrefix()
    {
        super("changeprefix", "changeprefix <prefix>", "Change the guild prefix to <prefix>");
    }

    @Override
    public void execute(CommandManager.ParsedCommandString commandString, GuildMessageReceivedEvent event, GuildConfiguration configuration)
    {
        if (event.getMember() == null)
            return;

        if (commandString.getArgs().length != 1)
        {
            // event.getChannel().sendMessage("Wrong argument count. See `help changeprefix` for usage.").queue();
            event.getChannel().sendMessage(this.getGenerator().generateErrorMsgWrongInput()).queue();
            return;
        }

        configuration.setPrefix(commandString.getArgs()[0]);
        new ConfigDaoImpl().writeConfig(configuration, event.getGuild().getIdLong());

        event.getChannel().sendMessage(this.getGenerator().generateSuccessMsg("Prefix has been changed to `" + configuration.getPrefix() + "`")).queue();
    }

    @Override
    public AccessLevel getAccessLevel()
    {
        return AccessLevel.OWNER;
    }
}
