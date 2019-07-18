package de.progen_bot.commands;

import de.mtorials.models.Warn;
import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.core.Main;
import de.progen_bot.db.MySQL;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;
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
        if (event.getMessage().getMentionedMembers().size() == 1) {
            List<String> warnTable = new ArrayList<>();

            for (Warn w : Main.getDAOWarnList().getWarnsByMember(event.getMessage().getMentionedMembers().get(0))) {

                warnTable.add(w.getReason());
            }

            if (!warnTable.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                EmbedBuilder eb = new EmbedBuilder().setTitle("WarnTable von " + event.getMember().getEffectiveName());
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
            event.getChannel().sendMessage(new EmbedBuilder().setColor(Color.red)
                    .setDescription("No user found").build()).queue();
        }
    }

    @Override
    public String help() {
        return null;
    }
}
