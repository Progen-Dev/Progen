package de.progen_bot.commands.xp;

import de.mtorials.config.GuildConfiguration;
import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.db.MySQL;
import de.progen_bot.db.UserData;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.List;

public class XPrank extends CommandHandler {
    public XPrank() {
        super("xprank","xprank","xprank");
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {

            List<String> top10Ids = MySQL.getTop10Ranks();

            StringBuilder sb = new StringBuilder();
            int i = 1;
            for (String m : top10Ids) {
                UserData tmpData = UserData.fromId(m);
                sb.append("``#" + i + "`` - " + event.getGuild().getMemberById(m).getAsMention() + " - Level "
                        + tmpData.getLevel() + "(" + tmpData.getTotalXp() + "XP)\n");
                i++;
            }

            event.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.cyan).setDescription(sb.toString()).build()).queue();

        }

    @Override
    public String help() {
        return null;
    }

}
