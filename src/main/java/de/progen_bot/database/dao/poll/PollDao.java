package de.progen_bot.database.dao.poll;

import de.progen_bot.database.entities.poll.PollData;

public interface PollDao
{
    void savePollData(PollData data);

    PollData getPollData(String messageId);

    void loadPollTimer();
}
