package de.progen_bot.commands.administrator;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.db.dao.config.ConfigDaoImpl;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.permissions.AccessLevel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ChangePrefix extends CommandHandler {

    public ChangePrefix() {
        super("changeprefix", "changeprefix <new prefix>", "Changes the bot's prefix");
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {

        if (event.getMember() == null)
            return;

        if (!event.getMember().isOwner()) {

            event.getTextChannel().sendMessage("You have to be the owner of that guild to change the prefix!").queue();
            return;
        }

        if (parsedCommand.getArgsAsList().size() != 1) {

            event.getTextChannel().sendMessage("See help command for usage. Wrong argument count.").queue();
            return;
        }

        configuration.setPrefix(parsedCommand.getArgsAsList().get(0));

        new ConfigDaoImpl().writeConfig(configuration, event.getGuild());

        event.getTextChannel().sendMessage("Prefix has changed to " + configuration.getPrefix()).queue();
    }

    @Override
    public AccessLevel getAccessLevel() {
        return AccessLevel.OWNER;
    }
}
