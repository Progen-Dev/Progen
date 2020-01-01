package de.progen_bot.db.dao.warnlist;

import net.dv8tion.jda.api.entities.Member;

import java.util.List;

public interface WarnListDao {
    void insertWarn(Member member, String reason);

    void insertWarnCount(String username, int count);

    int loadWarnCount(String username);

    List<String> loadWarnList(Member member);
}
