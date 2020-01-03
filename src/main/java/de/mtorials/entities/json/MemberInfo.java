package de.mtorials.entities.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MemberInfo {

    private Member member;

    public MemberInfo(Member member) {

        this.member = member;
    }

    @JsonIgnore
    public Member getMember() {
        return member;
    }

    public String getName() {
        return member.getUser().getName();
    }

    public String getTag() {

        return member.getUser().getDiscriminator();
    }

    public String getNameAndTag() {

        return member.getUser().getName() + "#" + member.getUser().getDiscriminator();
    }

    public String getGuildJoinDate() {

        return member.getTimeJoined().format(DateTimeFormatter.RFC_1123_DATE_TIME);
    }

    public String getDiscordJoinDate() {

        return member.getUser().getTimeCreated().format(DateTimeFormatter.RFC_1123_DATE_TIME);
    }

    public String getUserID() {

        return member.getUser().getId();
    }

    public String getOnlineStatus() {

        return member.getOnlineStatus().name();
    }

    @JsonIgnore
    public List<Role> getRoles() {

        return member.getRoles();
    }

    public ArrayList<String> getRoleNames() {

        //if (member.getRoles().size() == 0) throw new UserHasNoRoleExceotion();
        ArrayList<String> s = new ArrayList<>();
        for (Role r : member.getRoles()) {
            s.add(r.getName());
        }
        return s;
    }
}
