package de.progen_bot.commands.user;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager.ParsedCommandString;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.util.Statics;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.text.SimpleDateFormat;

import java.util.Date;

public class CommandPing extends CommandHandler {
    public CommandPing() {
        super("ping", "ping", "get the bot ping");
    }

    private String getTime(Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }

    private String getTimeDiff(Date date1, Date date2) {
        long diff = date1.getTime() - date2.getTime();
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);
        return diffDays + " d, " + parseTimeNumbs(diffHours) + " h, " + parseTimeNumbs(diffMinutes) + " min, " + parseTimeNumbs(diffSeconds) + " sec";
    }

    private String parseTimeNumbs(long time) {
        String timeString = time + "";
        if (timeString.length() < 2)
            timeString = "0" + time;
        return timeString;
    }

    @Override
    public void execute(ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {
        event.getTextChannel().sendMessage(
                new EmbedBuilder()
                        .setColor(new Color(255, 71,0))
                        .setDescription(":alarm_clock:   **UPTIME**")
                        .addField("Last restart", getTime(Statics.lastRestart, "dd.MM.yyyy - HH:mm:ss (z)"), false)
                        .addField("Online since", getTimeDiff(new Date(), Statics.lastRestart), false)
                        .addField("Reconnects since last restart", Statics.reconnectCount + "", false)
                        .build()
        ).queue();


    }
    @Override
    public String help() {
        return null;
    }
}
