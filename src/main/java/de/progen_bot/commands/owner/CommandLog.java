package de.progen_bot.commands.owner;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.db.dao.channel.ChannelDaoImpl;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.permissions.AccessLevel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandLog extends CommandHandler {
    public CommandLog() {
        super("log", "log", "Logbook for the owners of Progen.");
    }

    private void setLogChannel(MessageReceivedEvent event){
        TextChannel channel = null;

        if (event.getMessage().getMentionedChannels().size() == 1){
            channel = event.getMessage().getMentionedChannels().get(0);
        }

        ChannelDaoImpl channelDao = new ChannelDaoImpl();
        channelDao.addLogChannel(channel);
    }

    private void setStarBoardChannel(MessageReceivedEvent event){
        TextChannel channel = null;

        if (event.getMessage().getMentionedChannels().size() == 1){
            channel = event.getMessage().getMentionedChannels().get(0);
        }

        ChannelDaoImpl channelDao = new ChannelDaoImpl();
        channelDao.addStarBoardChannel(channel);
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {

        switch (parsedCommand.getArgs()[0]){
            case "logchannel":
                setLogChannel(event);
                break;
            case "starboard":
                setStarBoardChannel(event);
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
