package de.progen_bot.permissions;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PermissionCore {

    private final Member member;

    public PermissionCore(MessageReceivedEvent event) {
        member = event.getMember();
    }

    public AccessLevel getAccessLevel() {

        if (member == null)
            return AccessLevel.USER;

        if (member.getId().equals("402140322525872138") || member.getId().equals("279271145205923847"))
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
