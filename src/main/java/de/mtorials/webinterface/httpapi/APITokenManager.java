package de.mtorials.webinterface.httpapi;

import de.mtorials.webinterface.exceptions.APIUserNotRegistered;
import de.progen_bot.core.Main;
import net.dv8tion.jda.core.entities.Member;

import java.sql.SQLException;
import java.util.HashMap;

public class APITokenManager {

    private TokenManagerDAO dao = new TokenManagerDAO();

    public APITokenManager() {

        dao.generateTables();
    }

    public String register(Member member) {

        String randomString = generateRandomString();
        try {
            if (dao.keyExists(randomString)) return register(member);
            if (dao.memberExists(member)) dao.deleteMember(member);
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
        dao.addMember(randomString, member);
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
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }
}
