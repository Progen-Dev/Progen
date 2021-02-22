package de.progen_bot.database.dao.warnlist;

import net.dv8tion.jda.api.entities.Member;

import java.util.List;

public interface WarnListDao
{
    void insertWarn(Member member, String reason);

    List<String> loadWarnList(Member member);

    void deleteWarn(Member member, String reason);
}
