package de.mtorials.models;

import net.dv8tion.jda.core.entities.Member;

import java.time.format.DateTimeFormatter;

public class MemberInfo {

    private Member member;

    public MemberInfo(Member member) {

        this.member = member;
    }

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

        return member.getJoinDate().format(DateTimeFormatter.RFC_1123_DATE_TIME);
    }

    public String getDiscordJoinDate() {

        return member.getUser().getCreationTime().format(DateTimeFormatter.RFC_1123_DATE_TIME);
    }

    public String getUserID() {

        return member.getUser().getId();
    }

    public String getOnlineStatus() {

        return member.getOnlineStatus().name();
    }
}
