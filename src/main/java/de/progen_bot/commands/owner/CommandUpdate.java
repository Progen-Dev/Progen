package de.progen_bot.commands.owner;

import java.awt.Color;
import java.time.Instant;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager.ParsedCommandString;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.permissions.AccessLevel;
import de.progen_bot.util.Statics;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandUpdate extends CommandHandler {

    public CommandUpdate() {
        super("update", "update", "a simple embed to display the latest updates of Progen.");
    }
    @Override
    public void execute(ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {
        event.getTextChannel().sendMessage(
            new EmbedBuilder()
            .setColor(Color.blue)
            .setTitle("Update")
            .setDescription("Hey @everyone, `" + Statics.VERSION + "` is released.\n" + "https://github.com/Progen-Dev/Progen/releases/")
            .addField("Version", Statics.VERSION , false)
            .setTimestamp(Instant.now())
         .build()
        ).queue();
    }

    @Override
    public AccessLevel getAccessLevel(){
        return AccessLevel.BOT_OWNER;
    }

}