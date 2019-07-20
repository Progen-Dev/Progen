package de.progen_bot.commands.Moderator;

import de.mtorials.models.Warn;
import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.core.Main;
import de.progen_bot.db.MySQL;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WarnList extends CommandHandler {
    /**
     * Instantiates a new de.progen_bot.command handler.
     */
    public WarnList() {
        super("warnlist", "warnlist", "warnlist"); //TODO warnlist desc
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event) {
        System.out.println("[Info] Command pb!warnlist wird ausgeführt!");
        if (event.getMessage().getMentionedMembers().size() == 1) {
            List<String> warnTable = new ArrayList<>();

            for (Warn w : super.getDAOs().getWarnList().getWarnsByMember(event.getMessage().getMentionedMembers().get(0))) {

                warnTable.add(w.getReason());
            }

            if (!warnTable.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                EmbedBuilder eb = new EmbedBuilder().setTitle("WarnTable of " + event.getMember().getEffectiveName()).setColor(Color.ORANGE);
                int count = 1;
                for (String reason : warnTable) {
                    sb.append(count + ". " + reason + "\n");
                    count++;
                }
                event.getTextChannel().sendMessage(eb.setDescription(sb.toString()).build()).queue();
            } else {
                event.getChannel().sendMessage(new EmbedBuilder().setColor(Color.red)
                        .setDescription("The user has no warns yet").build()).queue();
            }
        } else {

            HashMap<Member, ArrayList<Warn>> x = super.getDAOs().getWarnList().getWarnsByMembersForGuild(event.getGuild());

            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .setTitle("The warns board of your guild")
                    .setColor(Color.ORANGE);

            for (Member m : x.keySet()) {

                embedBuilder.addField(m.getEffectiveName(), String.valueOf(x.get(m).size()), true);
            }

            event.getMessage().getTextChannel().sendMessage(embedBuilder.build()).queue();
        }
    }

    @Override
    public String help() {
        return null;
    }
}
