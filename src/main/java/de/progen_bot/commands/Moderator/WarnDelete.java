package de.progen_bot.commands.Moderator;

import de.mtorials.config.GuildConfiguration;
import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class WarnDelete extends CommandHandler {
    public WarnDelete() {super("warndelete","warndelete <user> <reason>","warned a user");
}

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {
        System.out.println("[INFO] Command pb!WarnDelete wurde ausgeführt!");
        super.getDAOs().getWarnList().deleteWarnForMember(event.getMessage().getMentionedMembers().get(0));

        if (parsedCommand.getArgs().length <= 1) {
            event.getChannel().sendMessage(new EmbedBuilder().setColor(Color.red).setDescription("No user found").build())
                    .queue();
            if (parsedCommand.getArgs().length <= 1) {
                event.getChannel().sendMessage(new EmbedBuilder().setColor(Color.red).setDescription("No warns found").build()).queue();
            }

        }
    }

    @Override
    public String help() {
        return null;
    }
}
