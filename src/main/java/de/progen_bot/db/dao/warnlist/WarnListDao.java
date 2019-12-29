package de.progen_bot.db.dao.warnlist;

import java.util.List;

public interface WarnListDao {
    void insertWarn(String username, String reason);

    void insertWarnCount(String username, int count);

    int loadWarnCount(String username);

    List<String> loadWarnList(String userId);
}
