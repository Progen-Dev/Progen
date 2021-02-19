package de.progen_bot.api.entities;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

public class MemberInfo
{
    private final Member member;

    public MemberInfo(Member member)
    {
        this.member = member;
    }

    @JsonIgnore
    public Member getMember()
    {
        return this.member;
    }

    public String getName()
    {
        return this.member.getUser().getName();
    }

    public String getDiscriminator()
    {
        return this.member.getUser().getDiscriminator();
    }

    public String getTag()
    {
        return this.member.getUser().getAsTag();
    }

    public String getGuildJoinDate()
    {
        return this.member.getTimeJoined().format(DateTimeFormatter.RFC_1123_DATE_TIME);
    }

    public String getDiscordJoinDate()
    {
        return this.member.getUser().getTimeCreated().format(DateTimeFormatter.RFC_1123_DATE_TIME);
    }

    public String getOnlineStatus()
    {
        return this.member.getOnlineStatus().name();
    }

    @JsonIgnore
    public List<Role> getRoles()
    {
        return this.member.getRoles();
    }

    public List<String> getRoleNames()
    {
        return this.member.getRoles().stream().map(Role::getName).collect(Collectors.toList());
    }
}
