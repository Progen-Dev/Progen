package de.progen_bot.db.dao.xp;

import de.progen_bot.db.entities.UserData;

import java.util.List;

public interface XpDao {
    void saveUserData(UserData data);

    UserData loadFromId(String userId);

    List<String> getTop10Ranks();
}
