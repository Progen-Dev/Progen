package de.progen_bot.commands.owner;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.permissions.AccessLevel;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Timer;
import java.util.TimerTask;

public class CommandStop extends CommandHandler {
    public CommandStop() {
        super("stop", "stop", "Only Progens owner can use this command!");

    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {


        event.getMessage().delete().queue();
        Message msg = event.getTextChannel()
                .sendMessage(super.messageGenerators.generateWarningMsg("Progen shuts down")).complete();


        new Timer().schedule(new TimerTask() {

            @Override

            public void run() {

                msg.delete().queue();

                System.exit(0);

            }

        }, 3000);

    }

    @Override
    public AccessLevel getAccessLevel() {
        return AccessLevel.BOT_OWNER;
    }

}

