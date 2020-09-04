package de.progen_bot.commands.fun;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.permissions.AccessLevel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PingPong extends CommandHandler {
    public PingPong() {
        super("ping", "pingpong", "pingpong the game");
    }
    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand , MessageReceivedEvent event , GuildConfiguration configuration){
        event.getTextChannel().sendMessage(":ping_pong: Pong!")
                .queue();

    }

    @Override
    public AccessLevel getAccessLevel() {
        return AccessLevel.USER;
    }
}
