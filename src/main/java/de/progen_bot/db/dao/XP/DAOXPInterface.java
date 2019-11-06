package de.progen_bot.db.dao.XP;

import de.progen_bot.commands.xp.XP;

import java.lang.reflect.Member;

public interface DAOXPInterface {
    XP getXPForMember(Member member);

}
