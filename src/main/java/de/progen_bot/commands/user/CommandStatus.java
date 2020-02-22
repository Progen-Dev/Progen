package de.progen_bot.commands.user;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager.ParsedCommandString;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.permissions.AccessLevel;
import de.progen_bot.util.Statics;
import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.text.SimpleDateFormat;

import java.util.Date;

public class CommandStatus extends CommandHandler {
    public CommandStatus() {
        super("status", "status", "Status of Progen");
    }

    private String getTime(Date date, String pattern){
        return new SimpleDateFormat(pattern).format(date);
    }

    private String getTimeDiff(Date date1, Date date2){
        long diff = date1.getTime() - date2.getTime();
        long DiffSecond = diff / 100 % 60;
        long DiffMinutes = diff / (60 * 1000) % 60;
        long DiffHours = diff / (24 * 60 * 60 * 1000) % 60;
        long DiffDays = diff / (24 * 60 * 60 * 1000);
        return DiffDays + "d, " + parseTimeNumbs(DiffHours) + "h, " + parseTimeNumbs(DiffMinutes) + parseTimeNumbs(DiffSecond) + "sec";
    }

    private String parseTimeNumbs(long time){
        String timeString = time + "";
        if (timeString.length() < 2)
            timeString = "0" + time;
        return timeString;
    }


    @Override
    public void execute(ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {
        event.getTextChannel().sendMessage(
                new EmbedBuilder()
                        .setDescription(":timer: ***STATUS***")
                        .setColor(new Color(187, 195, 255))
                        .addField("Ping", String.valueOf(event.getJDA().getGatewayPing()), true)
                        .addField("Last Restart", getTime(Statics.lastRestart, "dd.MM.yyyy - HH:mm:ss (z)"), false)
                        .addField("autoReconnects", Statics.reconnectCount + "", false)
                        .build()
        ).queue();
        SpeedTestSocket Dspeed = new SpeedTestSocket();
        SpeedTestSocket Uspeed = new SpeedTestSocket();
        StringBuilder sb = new StringBuilder();

        Message msg = event.getTextChannel().sendMessage(new EmbedBuilder().setDescription("**Speed test running...**\n\nTesting downstream with 10MB file...").build()).complete();

        Dspeed.addSpeedTestListener(new ISpeedTestListener() {
            
            @Override
            public void onCompletion(SpeedTestReport report) {
                sb.append("Downstream:  " + (report.getTransferRateBit().floatValue() / 1024 / 1024) + " MBit/s\n");
                msg.editMessage(new EmbedBuilder().setDescription("**Speed test running...**\n\nTesting upstream with 1MB file...").build()).queue();
                Uspeed.startUpload("https://testdebit.info/", 1000000);
            }

            @Override
            public void onProgress(float percent, SpeedTestReport report) {
            }

            @Override
            public void onError(SpeedTestError speedTestError, String s) {
                System.out.println(speedTestError);
            }

        });

        Uspeed.addSpeedTestListener(new ISpeedTestListener() {
            @Override
            public void onCompletion(SpeedTestReport report) {
                sb.append("Upstream:    " + (report.getTransferRateBit().floatValue() / 1024 / 1024) + " MBit/s");
                msg.editMessage(new EmbedBuilder().setDescription("**Speed test finished.**\n\n```" + sb.toString() + "```").build()).queue();
            }

            @Override
            public void onProgress(float v, SpeedTestReport speedTestReport) {

            }

            @Override
            public void onError(SpeedTestError speedTestError, String s) {
                System.out.println(speedTestError);
            }

        });

        Dspeed.startDownload("https://testdebit.info/");


    }

    @Override
    public String help() {
        return null;
    }

    @Override
    public AccessLevel getAccessLevel() {
        return AccessLevel.USER;
    }
}
