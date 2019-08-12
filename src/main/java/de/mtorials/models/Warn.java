package de.mtorials.models;

import de.mtorials.fortnite.core.User;
import net.dv8tion.jda.api.entities.Member;

public class Warn {

    private String reason;
    private User user;

    public Warn(Member member, String reason) {

        this.reason = reason;
        this.user = user;
    }

    public String getReason() {
        return reason;
    }

    public User getUser() {
        return user;
    }
}
