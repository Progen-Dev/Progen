package de.progen_bot.commands.Administartor;

import de.mtorials.config.GuildConfiguration;
import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.core.PermissionCore;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Timer;
import java.util.TimerTask;

public class CommandRestart extends CommandHandler {
    public CommandRestart() {
        super("restart", "restart", "Restart Progen. Only for Owner!");
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {
        if (PermissionCore.check(4, event)) return;

        event.getMessage().delete().queue();
        Message msg = event.getTextChannel()
                .sendMessage(super.messageGenerators.generateWarningMsg("Progen restarts")).complete();

        new Timer().schedule(new TimerTask() {

            @Override

            public void run() {

                msg.delete().queue();

                System.exit(0);

            }

        }, 3000);

    }

    @Override
    public String help() {
        return null;
    }
}
