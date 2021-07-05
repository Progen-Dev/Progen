package de.progen_bot.listeners;

import de.progen_bot.db.dao.Messages.MessageDaoImpl;
import de.progen_bot.db.entities.MessageBuilder;
import de.progen_bot.db.entities.MessageData;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class MessageListener extends ListenerAdapter {

    public void onGuildJoin(@Nonnull GuildJoinEvent event) {
        final MessageDaoImpl messageDao = new MessageDaoImpl();

        MessageData messageData = messageDao.loadWelcomeConfig(event.getGuild());

        if (messageData == null) {
            messageData = new MessageBuilder().setWelcomeMessage(null).setWelcomeMessageChannel(null).build();
            messageDao.addWelcomeMessage(messageData , event.getGuild());
        }

        String chatId = messageData.getWelcomeMessage();
    }

    public void onGuildLeave(@Nonnull GuildLeaveEvent event) {
        final MessageDaoImpl messageDao = new MessageDaoImpl();

        MessageData messageData = messageDao.loadLeaveConfig(event.getGuild());

        if (messageData == null){
            messageData = new MessageBuilder().setLeaveMessage(null).setLeaveMessageChannel(null).build();
            messageDao.addLeaveMessage(messageData, event.getGuild());
        }
    }
}
