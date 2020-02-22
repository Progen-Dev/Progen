package de.progen_bot.permissions;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

public class PermissionCore {

    Guild guild;
    Member member;

    public PermissionCore(MessageReceivedEvent event) {
        guild = event.getGuild();
        member = event.getMember();
    }

    public AccessLevel getAccessLevel() {

        if (member.equals(member.getGuild().getMemberById("402140322525872138")) || member.equals(member.getGuild().getMemberById("279271145205923847")))
            return AccessLevel.BOTOWNER;

        if (member.isOwner())
            return AccessLevel.OWNER;

        if (member.hasPermission(Permission.ADMINISTRATOR))
            return AccessLevel.ADMINISTRATOR;

        if (member.hasPermission(Permission.MESSAGE_MANAGE))
            return AccessLevel.MODERATOR;

        return AccessLevel.USER;
    }

}
