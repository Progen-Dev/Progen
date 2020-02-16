package de.progen_bot.util;


import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;

public class MessageGenerator {

    private String commandUsage;
    private String invokeString;

    public MessageGenerator(String commandUsage, String invokeString) {
        this.commandUsage = commandUsage;
        this.invokeString = invokeString;
    }

    /**
     * The error.
     *
     * @param error
     */
    public MessageEmbed generateErrorMsg(String error) {

        return new EmbedBuilder()
                .setColor(Color.RED)
                .setDescription(":no_entry: " + error + ":no_entry:")
                .build();
    }

    public MessageEmbed generateErrorMsgWrongInput() {

        return new EmbedBuilder()
                .setColor(Color.RED)
                .setTitle("ERROR")
                .addField("Use the command like this:", commandUsage, false)
                .build();
    }

    public MessageEmbed generateSuccessfulMsg() {

        return new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle("SUCCESSFULLY")
                .setDescription("Successfully executed command " + invokeString + ".")
                .build();
    }

    /**
     * The warning
     *
     * @param warning
     * @return
     */
    public MessageEmbed generateWarningMsg(String warning) {
        return new EmbedBuilder()
                .setColor(Color.BLUE)
                .setTitle("INFO")
                .setDescription(":warning: " + warning + " :warning:")
                .build();
    }

    public MessageEmbed generateInfoMsg(String infomsg) {
        return new EmbedBuilder()
                .setColor(Color.BLUE)
                .setTitle("INFO")
                .setDescription(" " + infomsg + "")
                .build();
    }

    /**
     * Right
     *
     * @param right
     */
    public MessageEmbed generateRightMsg(String right) {
        return new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle("RIGHT")
                .setDescription("" + right + " :white_check_mark:")
                .build();
    }
}
