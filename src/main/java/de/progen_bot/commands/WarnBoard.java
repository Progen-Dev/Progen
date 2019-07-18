package de.progen_bot.commands;

import de.mtorials.models.Warn;
import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.core.Main;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class WarnBoard extends CommandHandler {

    public WarnBoard() {
        super("warnboard", "warnboard", "warnboard");
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event) {

        StringBuilder msg = new StringBuilder();

        HashMap<Member, ArrayList<Warn>> x = Main.getDAOWarnList().getWarnsByMembersForGuild(event.getGuild());

        for (Member m : x.keySet()) {

            msg.append(m.getNickname()).append(": ").append(x.get(m).size()).append("\n");
        }

        event.getMessage().getTextChannel().sendMessage(msg).queue();
    }

    @Override
    public String help() {return null;}
}
