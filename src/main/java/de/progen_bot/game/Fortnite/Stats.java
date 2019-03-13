package de.progen_bot.game.Fortnite;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Stats extends CommandHandler {
    public Stats(){super("stats", "stats <user>","Fortnite stats");}
    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand , MessageReceivedEvent event) {


    }

    @Override
    public String help() {
        return null;
    }
}
