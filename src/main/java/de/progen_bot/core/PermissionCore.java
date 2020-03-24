package de.progen_bot.core;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

public class PermissionCore {

    private PermissionCore() {
        /* Prevent instantiation */
    }

    public static int getLevel(Member member) {

        if (member.equals(member.getGuild().getMemberById("402140322525872138")))
            return 4;

        if (member.equals(member.getGuild().getMemberById("279271145205923847")))
            return 4;

        if (member.isOwner())
            return 3;

        if (member.hasPermission(Permission.ADMINISTRATOR))
            return 2;

        if (member.hasPermission(Permission.MESSAGE_MANAGE))
            return 1;

        return 0;
    }

    public static boolean check(int level, MessageReceivedEvent event) {
        if (event.getMember() == null)
            return false;

        if (level > getLevel(event.getMember())) {
            event.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.red).setDescription(
                    "I'm sorry, but you do not have permission to use this command"
            ).build()).queue();
            return true;
        }
        return false;
    }

}
