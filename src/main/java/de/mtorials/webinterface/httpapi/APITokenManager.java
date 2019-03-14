package de.mtorials.webinterface.httpapi;

import de.mtorials.webinterface.exceptions.APIUserNotRegistered;
import net.dv8tion.jda.core.entities.Member;

import java.util.HashMap;

public class APITokenManager {

    private HashMap<String, Member> membersByToken = new HashMap<>();

    public String register(Member member) {

        String randomString = generateRandomString();
        if (this.membersByToken.containsKey(randomString)) return register(member);
        this.membersByToken.put(randomString, member);
        return randomString;
    }

    public Member getMember(String token) {

        if (!this.membersByToken.containsKey(token)) throw new APIUserNotRegistered();
        return membersByToken.get(token);
    }

    public HashMap<String, Member> getMembers() {
        return this.membersByToken;
    }

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
