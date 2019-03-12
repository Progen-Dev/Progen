package de.progen_bot.commands;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

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
