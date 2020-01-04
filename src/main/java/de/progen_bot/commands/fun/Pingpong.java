package de.progen_bot.commands.fun;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.db.entities.config.GuildConfiguration;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Pingpong extends CommandHandler {
    public Pingpong() {
        super("ping", "pingpong", "pingpong the game");
    }
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
