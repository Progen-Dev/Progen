package de.progen_bot.database.dao.connectfour;

import de.progen_bot.database.entities.connectfour.ConnectFourModel;

public interface ConnectFourDao
{
    ConnectFourModel getConnectFourData(String msgId);

    void insertConnectFourData(ConnectFourModel data);
}
