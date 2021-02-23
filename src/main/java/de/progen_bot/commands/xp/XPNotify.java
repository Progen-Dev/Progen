package de.progen_bot.commands.xp;

import de.progen_bot.core.command.CommandHandler;
import de.progen_bot.core.command.CommandManager;
import de.progen_bot.database.dao.xp.XpDaoImpl;
import de.progen_bot.database.entities.config.GuildConfiguration;
import de.progen_bot.database.entities.xp.UserData;
import de.progen_bot.utils.permission.AccessLevel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class XPNotify extends CommandHandler
{
    public XPNotify()
    {
        super("xpnotify", "xpnotify", "Toggle level up notification");
    }

    @Override
    public void execute(CommandManager.ParsedCommandString commandString, GuildMessageReceivedEvent event, GuildConfiguration configuration)
    {
        final UserData data = UserData.fromId(event.getAuthor().getId());

        if (data != null)
        {
            final boolean b = !data.getLvlUpNotify();
            data.setLvlUpNotify(b);

            event.getChannel().sendMessage(this.getGenerator().generateSuccessMsg("Successfully " + (b ? "enabled" : "disabled") + " level up notification")).queue();

            new XpDaoImpl().saveUserData(data);
        }
    }

    @Override
    public AccessLevel getAccessLevel()
    {
        return AccessLevel.USER;
    }
}
