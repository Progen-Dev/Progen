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

    private String getTime(Date date){
        return new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss (z)").format(date);
    }

    @Override
    public void execute(ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {
        event.getTextChannel().sendMessage(
                new EmbedBuilder()
                        .setDescription(":timer: ***STATUS***")
                        .setColor(new Color(187, 195, 255))
                        .addField("Ping", event.getJDA().getGatewayPing() + "ms", true)
                        .addField("Last Restart", getTime(Statics.getLastRestart()), false)
                        .addField("autoReconnects", Statics.getReconnectCount() + "", false)
                        .build()
        ).queue();
        SpeedTestSocket Dspeed = new SpeedTestSocket();
        SpeedTestSocket Uspeed = new SpeedTestSocket();
        StringBuilder sb = new StringBuilder();

        Message msg = event.getTextChannel().sendMessage(new EmbedBuilder().setDescription("**Speed test running...**\n\nTesting downstream with 10MB file...").build()).complete();

        Dspeed.addSpeedTestListener(new ISpeedTestListener() {

            @Override
            public void onCompletion(SpeedTestReport report) {
                sb.append("Downstream:  ").append(report.getTransferRateBit().floatValue() / 1024 / 1024).append(" MBit/s\n");
                msg.editMessage(new EmbedBuilder().setDescription("**Speed test running...**\n\nTesting upstream with 10MB file...").build()).queue();
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
                sb.append("Upstream:    ").append(report.getTransferRateBit().floatValue() / 1024 / 1024).append(" MBit/s");
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
    public AccessLevel getAccessLevel() {
        return AccessLevel.USER;
    }
}
