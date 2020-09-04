package de.progen_bot.db.dao.connectfour;

import de.progen_bot.db.entities.ConnectFourModel;
import de.progen_bot.db.entities.GameData;

public interface ConnectFourDao {
    ConnectFourModel getConnectFourData(String msgId);

    void insertConnectFourData(ConnectFourModel data);

    void insertGameData(GameData game);

    GameData getGameData(String messageId);
}
