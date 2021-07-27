package de.progen_bot.db.dao.channel;

import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public interface ChannelDao {

    void addLogChannel(TextChannel channel);
    List<String> loadLogChannel(TextChannel channel);
    void deleteLogChannel(TextChannel channel);

    void addStarBoardChannel(TextChannel channel);
    List<String> loadStarBoardChannel(TextChannel channel);
    void deleteStarBoardChannel(TextChannel channel);
}
