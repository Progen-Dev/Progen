package de.progen_bot.db.dao.messages;

import de.progen_bot.db.entities.GameData;
import de.progen_bot.db.entities.MessageData;
import net.dv8tion.jda.api.entities.Guild;

public interface MessageDao {

    void addWelcomeMessage(MessageData messageData, Guild guild);
    MessageData loadWelcomeConfig(Guild guild);

    void addLeaveMessage(MessageData messageData, Guild guild);
    MessageData loadLeaveConfig(Guild guild);
}
