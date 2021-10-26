package de.progen_bot.commands.settings;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.db.dao.config.ConfigDaoImpl;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.permissions.AccessLevel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandLog extends CommandHandler {
    public CommandLog() {
        super("log", "log", "Logbook for the owners of Progen.");
    }

    private void setLogChannel(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration){
        if (!event.isFromGuild())
            return;

        if (!event.getMessage().getMentionedChannels().isEmpty()) {
            configuration.setLogChannelID(event.getMessage().getMentionedChannels().get(0).getId());
            event.getTextChannel().sendMessageEmbeds(super.messageGenerators.generateRightMsg("Sucessfully set the modchannel to `" + event.getMessage().getMentionedChannels().get(0).getName())
            ).queue();
        }else if(parsedCommand.getArgs().length == 1){
            long id;

            try {
                id = Long.parseLong(parsedCommand.getArgs()[0]);
            }
            catch (NumberFormatException ignored){
                id = 0;
            }

            if (id == 0){
                event.getChannel().sendMessageEmbeds(super.messageGenerators.generateErrorMsg("Please provide a Textchannel id. To disable the logs run `pb!log channel` with no argument.")
                ).queue();
                return;
            }
            event.getTextChannel().sendMessageEmbeds(messageGenerators.generateInfoMsg("Successfully set Logchannel to `" + id + "`." )
            ).queue();
        }else {
            configuration.setLogChannelID(null);
            event.getTextChannel().sendMessageEmbeds(messageGenerators.generateInfoMsg("Successfully deactivated the logchannel")
            ).queue();
        }
        new ConfigDaoImpl().writeConfig(configuration, event.getGuild());
    }

    private void setStarboardChannel(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration){
        if (!event.isFromGuild())
            return;

        if (!event.getMessage().getMentionedChannels().isEmpty()) {
            configuration.setStarBoardChannelID(event.getMessage().getMentionedChannels().get(0).getId());
            event.getTextChannel().sendMessageEmbeds(super.messageGenerators.generateRightMsg("Successfully set the starboard to `" + event.getMessage().getMentionedChannels().get(0).getName() + "`.")
            ).queue();
        }else if(parsedCommand.getArgs().length == 1){
            long id;

            try {
                id = Long.parseLong(parsedCommand.getArgs()[0]);
            }
            catch (NumberFormatException ignored){
                id = 0;
            }

            if (id == 0){
                event.getChannel().sendMessageEmbeds(super.messageGenerators.generateErrorMsg("Please provide a Textchannel id. To disable the starboard run `pb!log starboard` with no arguments.")
                ).queue();
                return;
            }
            event.getTextChannel().sendMessageEmbeds(messageGenerators.generateInfoMsg("Successfully set the starboard to `" + id + "`." )
            ).queue();
        }else {
            configuration.setStarBoardChannelID(null);
            event.getTextChannel().sendMessageEmbeds(messageGenerators.generateInfoMsg("Successfully deactivated the starboard.")
            ).queue();
        }
        new ConfigDaoImpl().writeConfig(configuration, event.getGuild());
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {
        switch (parsedCommand.getArgs()[0]){
            case "starboard":
                setStarboardChannel(parsedCommand, event, configuration);
                break;
            case "channel":
                setLogChannel(parsedCommand, event, configuration);
                break;
            default:
                this.messageGenerators.generateErrorMsgWrongInput();
                break;
        }
    }

    @Override
    public AccessLevel getAccessLevel() {
        return AccessLevel.BOT_OWNER;
    }

}
