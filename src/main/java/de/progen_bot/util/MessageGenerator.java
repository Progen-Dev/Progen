package de.progen_bot.util;


import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.Color;

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
     * @param error error message
     * @return {@link MessageEmbed error embed}
     */
    public MessageEmbed generateErrorMsg(String error) {

        return new EmbedBuilder()
                .setColor(Color.RED)
                .setDescription(" " + error + " ")
                .build();
    }

    /**
     * Wrong input error
     *
     * @return {@link MessageEmbed error embed}
     */
    public MessageEmbed generateErrorMsgWrongInput() {

        return new EmbedBuilder()
                .setColor(Color.RED)
                .setTitle("ERROR")
                .addField("Use the command like this:", commandUsage, false)
                .build();
    }

    /**
     * Success message
     *
     * @return {@link MessageEmbed success embed}
     */
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
     * @param warning warning message
     * @return {@link MessageEmbed warning embed}
     */
    public MessageEmbed generateWarningMsg(String warning) {
        return new EmbedBuilder()
                .setColor(Color.BLUE)
                .setTitle("INFO")
                .setDescription("" + warning + "")
                .build();
    }

    /**
     * Info message
     *
     * @param infomsg info message
     * @return {@link MessageEmbed info embed}
     */
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
     * @param right right message
     * @return {@link MessageEmbed right embed}
     */
    public MessageEmbed generateRightMsg(String right) {
        return new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle("RIGHT")
                .setDescription("" + right + " ")
                .build();
    }
}
