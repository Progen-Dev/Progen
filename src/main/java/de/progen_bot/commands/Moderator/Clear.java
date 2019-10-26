package de.progen_bot.commands.Moderator;

import de.mtorials.config.GuildConfiguration;
import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager.ParsedCommandString;
import de.progen_bot.core.PermissionCore;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class Clear extends CommandHandler {

    public Clear() {
        super("clear" , "clear <anzahl>" , "clear some messages");
    }

    private int getInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch (Exception exception) {
            exception.printStackTrace();
            return 0;
        }
    }

    @Override
    public void execute(ParsedCommandString parsedCommand , MessageReceivedEvent event , GuildConfiguration configuration) {

        if (PermissionCore.check(1 , event)) return;

    }


    @Override
    public String help() {
        return null;
    }
}
