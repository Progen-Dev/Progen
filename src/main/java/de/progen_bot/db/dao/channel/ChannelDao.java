package de.progen_bot.db.dao.channel;

import de.progen_bot.db.entities.channel.ChannelData;
import net.dv8tion.jda.api.entities.Guild;

public interface ChannelDao {
    void addLogChannelId(ChannelData channelData, Guild guild);
    ChannelData loadLogChannelId(ChannelData channelData, Guild guild);
    void deleteLogChannelId(ChannelData channelData, Guild guild);

    void addStarBoardChannelId(ChannelData channelData, Guild guild);
    ChannelData loadStarBoardChannelId(ChannelData channelData, Guild guild);
    void deleteStarBoardChannelId(ChannelData channelData, Guild guild);

    void addVoiceLogChannelId(ChannelData channelData, Guild guild);
    ChannelData loadVoiceLogChannelId(ChannelData channelData, Guild guild);
    void deleteVoiceLogChannelId(ChannelData channelData, Guild guild);
}
