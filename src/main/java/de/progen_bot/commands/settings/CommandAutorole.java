package de.progen_bot.commands.settings;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.db.dao.config.ConfigDaoImpl;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.permissions.AccessLevel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandAutorole extends CommandHandler{
    public CommandAutorole() {
        super("autorole", "autorole <@role>", "Set a roll that the user should get automatically when joining");
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {

        if (!event.isFromGuild())
            return;

        if (!event.getMessage().getMentionedRoles().isEmpty()){
            configuration.setAutoRole(event.getMessage().getMentionedRoles().get(0).getId());
            event.getTextChannel().sendMessage(
                    messageGenerators.generateRightMsg("Successfully set autorole to `" + event.getMessage().getMentionedRoles().get(0).getName() +  "`.")
            ).queue();
        }else if (parsedCommand.getArgs().length == 1){
            configuration.setAutoRole(parsedCommand.getArgs()[0]);
            event.getTextChannel().sendMessage(
                    messageGenerators.generateRightMsg("Successfully set autorole to `" + parsedCommand.getArgs()[0] + "`.")
            ).queue();
        } else {
            configuration.setAutoRole("");
            event.getTextChannel().sendMessage(messageGenerators.generateRightMsg("Successfully deactivated autorole.")).queue();
        }

        new ConfigDaoImpl().writeConfig(configuration, event.getGuild());

    }
    @Override
    public AccessLevel getAccessLevel() {
        return AccessLevel.MODERATOR;
    }

}
