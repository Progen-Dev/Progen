package de.progen_bot.commands.moderator;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager.ParsedCommandString;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.permissions.AccessLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;


public class Clear extends CommandHandler {

    final EmbedBuilder error = new EmbedBuilder().setColor(Color.RED);

    public Clear() {
        super("clear", "clear <number>", "clear some messages that are no older than two weeks");
    }

    private int getInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch (Exception exception) {
            exception.printStackTrace();
            return 0;
        }
    }

    @Override
    public void execute(ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {
        String[] args = parsedCommand.getArgs();

        if (args.length < 1) {
            event.getTextChannel().sendMessage(error
                    .setDescription("Please specify how many messages should be deleted.").build())
                    .queue();
            return;
        }
        int numb = getInt(args[0]);
        if (numb > 1 && numb <= 100) {
            event.getMessage().delete().queue();

            OffsetDateTime twoWeeksAgo = OffsetDateTime.now().minus(2, ChronoUnit.WEEKS);
            MessageHistory history = new MessageHistory(event.getTextChannel());
            List<Message> messages = history.retrievePast(numb).complete();

            List<Message> messagesTwoWeeksAgo =
                    messages.stream().filter(m -> m.getTimeCreated().isBefore(twoWeeksAgo)).collect(Collectors.toList());
            messages.removeIf(m -> m.getTimeCreated().isBefore(twoWeeksAgo));

            for (Message message : messagesTwoWeeksAgo) {
                message.delete().queue();
            }

            if (messages.size() == 1) {
                messages.get(0).delete().queue();
            } else if (messages.size() > 1) {
                event.getTextChannel().deleteMessages(messages).queue();
            }

            Message msg = event.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.GREEN)
                    .setDescription(messages.size() + messagesTwoWeeksAgo.size() + " deleted messages").build()).complete();

            new Timer().schedule(new TimerTask() {

                @Override
                public void run() {
                    msg.delete().queue();
                }
            }, 3000);

            System.out.println("[INFO]: There were " + messages.size() + " deleted messages");

        } else {
            event.getTextChannel()
                    .sendMessage(error.setDescription("Please use a number between 2 and 100!").build()).queue();
        }
   }

    @Override
    public AccessLevel getAccessLevel() {
        return AccessLevel.MODERATOR;
    }
}
