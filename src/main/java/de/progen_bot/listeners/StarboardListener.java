package de.progen_bot.listeners;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager.ParsedCommandString;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.permissions.AccessLevel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class StarboardListener extends CommandHandler {
    public StarboardListener(String invokeString, String commandUsage, String description) {
        super(invokeString, commandUsage, description);
    }

    @Override
	public void execute(ParsedCommandString parsedCommand, MessageReceivedEvent event,
			GuildConfiguration configuration) {

    // if (event.getReactionEmote().getName().equals("\u2B50")) { 
      //  event.getChannel().sendMessage(new MessageBuilder().setContent(count[0] + 
       // "\u2B50").setEmbed(embed).build().queue();
      //}
     }
    @Override
	public AccessLevel getAccessLevel() {
		return null;
	}
}