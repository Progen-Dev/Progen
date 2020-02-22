package de.progen_bot.commands.user;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.permissions.AccessLevel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import okhttp3.internal.http2.Http2Connection;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Scanner;

public class CommandDonate extends CommandHandler {
    private static final String LIST_URL = "https://progen-bot.de/Donations";
    private static float NEED_VAL = 20;
    private float allval = 0;
    public CommandDonate(){super("donate", "support progen", "Support us and Progen with a small donation.");
    }

    private void addAllval(float add){
        allval += add;
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {
        allval = 0;

        DecimalFormat decimalFormat = new DecimalFormat("##.##");
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
