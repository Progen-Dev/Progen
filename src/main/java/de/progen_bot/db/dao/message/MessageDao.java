package de.progen_bot.db.dao.message;

import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public interface MessageDao {

    void addWelcomeMessage(TextChannel channel, String message);
    List<String> loadWelcomeMessage(TextChannel channel);
    void deleteWelcomeMessage(TextChannel channel, String message);
}
