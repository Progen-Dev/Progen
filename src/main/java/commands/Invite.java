package commands;

import command.CommandHandler;
import command.CommandManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Timer;
import java.util.TimerTask;

public  class Invite extends CommandHandler {

    public Invite() {
        super("invite","invite","Invite Progen!");
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand , MessageReceivedEvent event) {
        event.getTextChannel().sendMessage("With this link, you can get Progen on your server: https://discordbots.org/bot/495293590503817237").queue();
        System.out.println("[Info] Command pb!invite wurde ausgeführt!");


    }
    @Override
    public String help() {
        return null;
    }
}
