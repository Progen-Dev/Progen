package de.progen_bot.permissions;

import java.util.List;

import de.progen_bot.util.Settings;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PermissionCore {

    private final Member member;

    public PermissionCore(MessageReceivedEvent event) {
        member = event.getMember();
    }

    // How about static method?
    public AccessLevel getAccessLevel() {
        final List<String> owners = Settings.BOT_OWNERS;

        if (member == null)
            return AccessLevel.USER;

        if (owners.stream().anyMatch(id -> id.equals(member.getId())))
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
