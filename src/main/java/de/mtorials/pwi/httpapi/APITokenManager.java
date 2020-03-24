package de.mtorials.pwi.httpapi;

import de.mtorials.pwi.exceptions.APIUserNotRegistered;
import de.progen_bot.db.dao.tokenmanager.TokenManagerDao;
import net.dv8tion.jda.api.entities.Member;

import java.sql.SQLException;

public class APITokenManager {

    private TokenManagerDao dao = new TokenManagerDao();

    public String register(Member member) {

        String randomString = generateRandomString();
        try {
            if (dao.keyExists(randomString)) return register(member);
            if (dao.memberExists(member)) dao.deleteMember(member);
            dao.addMember(randomString, member);
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
        return randomString;
    }

    public Member getMember(String token) {

        try {
            if (!dao.keyExists(token)) throw new APIUserNotRegistered();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            return dao.getMember(token);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Not mine
    private String generateRandomString() {

        int n = 10;

        // chose a Character random from this String
        final String alphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int) (alphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(alphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }
}
