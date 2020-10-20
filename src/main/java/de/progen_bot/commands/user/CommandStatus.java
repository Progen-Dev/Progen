package de.progen_bot.commands.user;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager.ParsedCommandString;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.permissions.AccessLevel;
import de.progen_bot.util.Statics;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandStatus extends CommandHandler {
    public CommandStatus() {
        super("status", "status", "Status of Progen");
    }

    public String getTime(Date date) {
        return new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss (z)").format(date);
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
            .setTitle("Status")
            .setColor(Color.GREEN)
            .setDescription("V" + Statics.VERSION)
            .addField("Uptime", getTimeDiff(new Date(), Statics.getLastRestart()), false)
            .addField("Ping", event.getJDA().getGatewayPing() + "ms", true)
            .addField("Reconnects", Statics.getReconnectCount() + "", false)
                        .build()
        ).queue();
    }

    @Override
    public AccessLevel getAccessLevel() {
        return AccessLevel.USER;
    }
}
