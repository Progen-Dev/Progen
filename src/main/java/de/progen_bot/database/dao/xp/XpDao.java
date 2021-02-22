package de.progen_bot.database.dao.xp;

import de.progen_bot.database.entities.xp.UserData;

import java.util.List;

public interface XpDao
{
    void saveUserData(UserData data);

    UserData loadFromId(String userId);

    List<String> getTop10Ranks();
}
