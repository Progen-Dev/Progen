package de.progen_bot.commands.moderator;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.db.dao.warnlist.WarnListDaoImpl;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.permissions.AccessLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.List;

public class WarnList extends CommandHandler {
    /**
     * Instantiates a new de.progen_bot.command handler.
     */
    public WarnList() {
        super("warnlist", "warnlist or warnlist<user>",
                "list all active alerts of your server in a table or retrieve custom alerts from specific users");
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event,
            GuildConfiguration configuration) {
        if (event.getMessage().getMentionedMembers().size() == 1) {
            Member mentioned = event.getMessage().getMentionedMembers().get(0);
            List<String> warnTable = new WarnListDaoImpl().loadWarnList(mentioned);

            if (!warnTable.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                EmbedBuilder eb = new EmbedBuilder().setTitle("WarnTable of " + mentioned.getEffectiveName())
                        .setColor(Color.ORANGE);
                int count = 1;
                for (String reason : warnTable) {
                    sb.append(count).append('.').append(' ').append(reason).append('\n');
                    count++;
                }
                event.getTextChannel().sendMessage(eb.setDescription(sb.toString()).build()).queue();
            } else {
                event.getChannel().sendMessage(super.messageGenerators.generateErrorMsg("No Warns")).queue();
            }
        } else {
            event.getTextChannel().sendMessage(super.messageGenerators.generateErrorMsg("No user found")).queue();
            return;
        }
    }

    @Override
    public AccessLevel getAccessLevel() {
        return AccessLevel.MODERATOR;
    }
}
