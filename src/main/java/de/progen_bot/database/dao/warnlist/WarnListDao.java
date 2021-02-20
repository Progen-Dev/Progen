package de.progen_bot.database.dao.warnlist;

import java.util.List;

public interface WarnListDao
{
    List<String> loadWarnList(long memberId);
}
