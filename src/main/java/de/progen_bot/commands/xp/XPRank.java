package de.progen_bot.commands.xp;

import de.progen_bot.core.command.CommandHandler;
import de.progen_bot.core.command.CommandManager;
import de.progen_bot.database.dao.xp.XpDaoImpl;
import de.progen_bot.database.entities.config.GuildConfiguration;
import de.progen_bot.database.entities.xp.UserData;
import de.progen_bot.utils.permission.AccessLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.List;

public class XPRank extends CommandHandler
{
    public XPRank()
    {
        super("xprank", "xprank", "Take a ");
    }

    @Override
    public void execute(CommandManager.ParsedCommandString commandString, GuildMessageReceivedEvent event, GuildConfiguration configuration)
    {
        final List<String> top10Ids = new XpDaoImpl().getTop10Ranks();
        final StringBuilder sb = new StringBuilder();

        int i = 1;
        for (String id : top10Ids)
        {
            final UserData data = UserData.fromId(id);
            final Member member = event.getGuild().getMemberById(id);

            if (member != null)
            {
                sb.append('`').append('#').append(i).append('`').append(" - ").append(member.getAsMention()).append(" - Level ").append(data.getLevel()).append(' ').append('(').append(data.getTotalXp()).append(" XP)").append('\n');
                i += 1;
            }
        }

        event.getChannel().sendMessage(new EmbedBuilder().setColor(Color.cyan).setDescription(sb.toString()).build()).queue();
    }

    @Override
    public AccessLevel getAccessLevel()
    {
        return AccessLevel.USER;
    }
}
