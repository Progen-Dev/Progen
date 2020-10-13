package de.progen_bot.db.dao.poll;

import de.progen_bot.db.entities.PollData;

public interface PollDao {

    void savePollData(PollData data);

    PollData getPollData(String messageId);

    void loadPollTimer();
}