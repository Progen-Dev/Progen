package de.progen_bot.commands.Fun;

import de.mtorials.config.GuildConfiguration;
import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.IOException;

public class pingpong extends CommandHandler {
    public pingpong(){super("ping", "pingpong", "pingpong the game");}
    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand , MessageReceivedEvent event , GuildConfiguration configuration){
        event.getTextChannel().sendMessage(":ping_pong: Pong!")
                .queue();

    }

    @Override
    public String help() {
        return null;
    }
}
