package de.progen_bot.commands.Moderator;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class WarnDelete extends CommandHandler {
    public WarnDelete() {super("warndelete","warndelete <user> <reason>","warned a user");
}

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event) {
        System.out.println("[INFO] Command pb!WarnDelete wurde ausgeführt!");
        super.getDAOs().getWarnList().deleteWarnForMember(event.getMessage().getMentionedMembers().get(0));

    }

    @Override
    public String help() {
        return null;
    }
}
