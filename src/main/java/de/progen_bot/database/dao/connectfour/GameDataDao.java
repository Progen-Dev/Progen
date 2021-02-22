package de.progen_bot.database.dao.connectfour;

import de.progen_bot.database.entities.connectfour.GameData;

public interface GameDataDao
{
    void insertGameData(GameData data);

    GameData getGameData(String msgId);
}
