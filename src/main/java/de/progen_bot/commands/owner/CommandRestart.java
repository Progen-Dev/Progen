package de.progen_bot.commands.owner;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.core.PermissionCore;
import de.progen_bot.db.entities.config.GuildConfiguration;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import javax.imageio.IIOException;
import java.io.IOException;

public class CommandRestart extends CommandHandler {
    public CommandRestart() {
        super("restart", "restart", "Restart Progen. Only for Owner!");
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {
        if (PermissionCore.check(4, event));
        event.getTextChannel().sendMessage(
         messageGenerators.generateWarningMsg("Progen will restart now..")
        ).queue();

        if (System.getProperty("os.name").toLowerCase().contains("Linux")) {
            try {
                Runtime.getRuntime().exec("screen python restart.py");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.exit(0);
    }

    @Override
    public String help() {
        return null;
    }
}
