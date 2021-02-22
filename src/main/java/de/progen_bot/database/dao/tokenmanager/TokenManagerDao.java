package de.progen_bot.database.dao.tokenmanager;

import net.dv8tion.jda.api.entities.Member;

import java.sql.SQLException;

public interface TokenManagerDao
{
    boolean keyExists(String token) throws SQLException;

    boolean memberExists(Member member) throws SQLException;

    void addMember(String token, Member member) throws SQLException;

    Member getMember(String token) throws SQLException;

    void deleteMember(Member member) throws SQLException;
}
