package de.progen_bot.commands.xp;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.db.dao.xp.XpDaoImpl;
import de.progen_bot.db.entities.UserData;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.permissions.AccessLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.List;

public class XPrank extends CommandHandler {
    public XPrank() {
        super("xprank", "xprank", "compare your xp with the servercommunity");
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {

        List<String> top10Ids = new XpDaoImpl().getTop10Ranks();

        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (String m : top10Ids) {
            UserData tmpData = UserData.fromId(m);

            final Member member = event.getGuild().getMemberById(m);
            if (member != null) {
                sb.append("``#").append(i).append("`` - ").append(member.getAsMention()).append(" - Level ").append(tmpData.getLevel()).append("(").append(tmpData.getTotalXp()).append("XP)\n");
                i++;
            }
        }

        event.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.cyan).setDescription(sb.toString()).build()).queue();
    }

    @Override
    public AccessLevel getAccessLevel() {
        return AccessLevel.USER;
    }

}
