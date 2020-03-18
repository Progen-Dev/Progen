package de.progen_bot.commands.owner;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.db.entities.config.GuildConfiguration;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.Date;

public class CommandTest extends CommandHandler {
    public CommandTest() {
        super("test", "test", "test");
    }

    private static long inputTime;
    private final String HELP = "USAGE: ~ping";

    public static void setInputTime(long inputTimeLong){
        inputTime = inputTimeLong;
    }

    private Color getColorByPing(long ping){
        if (ping < 100)
        return Color.green;
        if (ping < 400)
        return Color.cyan;
        if (ping < 700)
            return Color.yellow;
        if (ping < 1000)
            return Color.orange;
        return Color.red;
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {
        long processing = new Date().getTime() - inputTime;
        long ping = event.getJDA().getGatewayPing();
        event.getTextChannel().sendMessage(new EmbedBuilder().setColor(getColorByPing(ping)).setDescription(
                String.format(":ping_pong:   **Pong!**\n\nThe bot took `%s` milliseconds to response.\nIt took `%s` milliseconds to parse the command and the ping is `%s` milliseconds.",
                        processing + ping, processing, ping)
        ).build()).queue();

    }

    @Override
    public String help() {
        return null;
    }
}
