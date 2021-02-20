package de.progen_bot.api;

import de.progen_bot.api.exceptions.APIUserNotRegisteredException;
import de.progen_bot.database.dao.tokenmanager.TokenManagerDao;
import net.dv8tion.jda.api.entities.Member;

import java.sql.SQLException;
import java.util.stream.IntStream;

public class APITokenManager
{
    private final TokenManagerDao dao = new TokenManagerDao();

    private static String generateRandomString()
    {
        final int N = 10;
        final String alphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvwxyz";
        final StringBuilder sb = new StringBuilder(N);

        IntStream.range(0, N + 1).map(i -> (int) (alphaNumericString.length() * Math.random())).forEachOrdered(i -> sb.append(alphaNumericString.charAt(i)));

        return sb.toString();
    }

    public String register(Member member)
    {
        final String randomString = generateRandomString();

        try
        {
            if (this.dao.keyExists(randomString))
                return this.register(member);
            if (this.dao.memberExists(member))
                this.dao.deleteMember(member);

            this.dao.addMember(randomString, member);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return "";
        }

        return randomString;
    }

    public Member getMember(String token)
    {
        try
        {
            if (!this.dao.keyExists(token))
                throw new APIUserNotRegisteredException();

            return this.dao.getMember(token);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public TokenManagerDao getDao()
    {
        return this.dao;
    }
}
