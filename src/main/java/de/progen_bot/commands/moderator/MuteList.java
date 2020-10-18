package de.progen_bot.commands.moderator;

import java.awt.Color;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

            Map<Long, String> muteMap = mutes.stream()
                    .collect(Collectors.toMap(mute -> Long.parseLong(mute.getVictimId()), MuteData::getReason));

            event.getGuild().retrieveMembersByIds(muteMap.keySet()).onSuccess(members -> {
                members.forEach(member -> {
                    sb.append(count[0]).append('.').append(' ')
                            .append(member.getAsMention() + " " + muteMap.get(member.getIdLong())).append('\n');
                    count[0]++;
                });
                event.getTextChannel().sendMessage(eb.setDescription(sb.toString()).build()).queue();
            });
        } else {
            event.getChannel().sendMessage(super.messageGenerators.generateErrorMsg("No mutes found.")).queue();
        }
    }

    @Override
    public AccessLevel getAccessLevel() {
        return AccessLevel.MODERATOR;
    }
}

