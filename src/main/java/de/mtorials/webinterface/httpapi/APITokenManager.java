package de.mtorials.webinterface.httpapi;

import de.mtorials.webinterface.exceptions.APIUserNotRegistered;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Member;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Random;

public class APITokenManager {

    private JDA jda;
    private HashMap<String, Member> membersByToken = new HashMap<>();

    public APITokenManager(JDA jda) {

        this.jda = jda;
    }

    public String register(Member member) {

        String randomString = generateRandomString();
        if (this.membersByToken.containsKey(randomString)) return register(member);
        this.membersByToken.put(randomString, member);
        return randomString;
    }

    public Member getMember(String token) {

        if (this.membersByToken.containsKey(token)) throw new APIUserNotRegistered();
        return membersByToken.get(token);
    }

    private String generateRandomString() {

        byte[] array = new byte[7];
        new Random().nextBytes(array);
        return new String(array, Charset.forName("UTF-8"));
    }

}
