package de.progen_bot.commands.moderator;

import java.awt.Color;
import java.util.List;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.db.dao.mute.MuteDao;
import de.progen_bot.db.entities.MuteData;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.permissions.AccessLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MuteList extends CommandHandler {
    public MuteList() {
        super("mutelist", "mutelist", "List all mutes of your server in a table");
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event,
            GuildConfiguration configuration) {

        List<MuteData> mutes = new MuteDao().getMutesByGuild(event.getGuild().getId());

        if (!mutes.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            EmbedBuilder eb = new EmbedBuilder().setTitle("Mute table of " + event.getGuild().getName())
                    .setColor(Color.ORANGE);
            int[] count = { 1 };

            mutes.forEach(mute -> {
                sb.append(count[0]).append('.').append(' ').append(
                        event.getGuild().getMemberById(mute.getVictimId()).getAsMention() + " " + mute.getReason())
                        .append('\n');
                count[0]++;
            });

            event.getTextChannel().sendMessage(eb.setDescription(sb.toString()).build()).queue();
        } else {
            event.getChannel().sendMessage(super.messageGenerators.generateErrorMsg("No mutes found.")).queue();
        }
    }

    @Override
    public AccessLevel getAccessLevel() {
        return AccessLevel.MODERATOR;
    }
}
