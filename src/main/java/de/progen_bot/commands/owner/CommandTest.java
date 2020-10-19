package de.progen_bot.commands.owner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager.ParsedCommandString;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.permissions.AccessLevel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandTest extends CommandHandler {
    public CommandTest() {
        super("test", "test", "test");
    }

    private static final Pattern PATTERN = Pattern.compile("(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");

    @Override
    public void execute(final ParsedCommandString parsedCommand, final MessageReceivedEvent event,
            final GuildConfiguration configuration) {

                event.getTextChannel().sendMessage(super.messageGenerators.generateInfoMsg("WAIT....")).queue();

                    final ProcessBuilder pb = new ProcessBuilder("/bin/sh", "-c", "speedtest-cli --simple --share");
                    Process p;
                    try {
                        p = pb.start();
                    } catch (IOException e1) {
                     return;
                    }

        final BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        final StringBuilder builder = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null)
                builder.append(line).append(' ');
        } catch (final IOException e) {
                    e.printStackTrace();
                    System.out.println("B");
                }
    
            final String result = builder.toString();
    
            final Matcher matcher = PATTERN.matcher(result);
    
            String link = "none";
            if (matcher.find())
                link = matcher.group();
    
            System.out.println("\n" + link);
            }

    @Override
    public AccessLevel getAccessLevel() {
        return AccessLevel.OWNER;
    }

}