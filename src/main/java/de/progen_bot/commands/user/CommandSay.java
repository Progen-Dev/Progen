package de.progen_bot.commands.user;

import java.time.Instant;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager.ParsedCommandString;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.listeners.SayListener;
import de.progen_bot.listeners.SayModel;
import de.progen_bot.permissions.AccessLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.ErrorResponse;

public class CommandSay extends CommandHandler {

    public CommandSay() {
        super("say", "say <text>", "let the bot write a message");
    }

    @Override
    public void execute(ParsedCommandString parsedCommand, MessageReceivedEvent event,
            GuildConfiguration configuration) {

        event.getAuthor().openPrivateChannel()
                .flatMap(channel -> channel.sendMessage("Please choose a color in hex <#fff> or <#ffffff>"))
                .onErrorFlatMap(ErrorResponse.CANNOT_SEND_TO_USER::test,
                        (error) -> event.getTextChannel().sendMessage("Please enable your DM´s to use this command."))
                .queue(v -> {
                    final String out = String.join(" ", parsedCommand.getArgs());

                    final SayModel sayModel = new SayModel(event.getTextChannel().getId(), new EmbedBuilder()
                            .setDescription(out).setAuthor(event.getAuthor().getAsTag()).setTimestamp(Instant.now()));
                    SayListener.addSayModel(event.getAuthor().getId(), sayModel);
                });
    }

    @Override
    public AccessLevel getAccessLevel() {
        return AccessLevel.USER;
    }
}
