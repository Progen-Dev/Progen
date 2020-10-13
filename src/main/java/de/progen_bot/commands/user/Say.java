package de.progen_bot.commands.user;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager.ParsedCommandString;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.listeners.SayListener;
import de.progen_bot.listeners.SayModel;
import de.progen_bot.permissions.AccessLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Say extends CommandHandler {

    public Say() {
        super("say", "say <text>", "let the bot write a message");
    }

    @Override
    public void execute(ParsedCommandString parsedCommand, MessageReceivedEvent event,
            GuildConfiguration configuration) {

        StringBuilder out = new StringBuilder(" ");
        for (String s : parsedCommand.getArgs()) {
            out.append(s).append(" ");
        }

        SayModel sayModel = new SayModel(event.getTextChannel().getId(),
                new EmbedBuilder().setDescription(out.toString()));
        SayListener.addSayModel(event.getAuthor().getId(), sayModel);

        event.getAuthor().openPrivateChannel().queue(channel -> channel.sendMessage("Please choose a color in hex <#fff>").queue());
    }

    @Override
    public AccessLevel getAccessLevel() {
        return AccessLevel.USER;
    }
}
