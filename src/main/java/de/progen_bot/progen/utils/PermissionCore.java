package de.progen_bot.progen.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.time.Instant;

/**
 * Permission handler of Progen
 *
 * @author Progen-Dev
 * @since 2.0.0
 */
public class PermissionCore
{
    /**
     * Private constructor to hide no-args constructor of utility class
     */
    private PermissionCore()
    {
        /* Prevent instantiation */
    }

    public static int getLevel(Member member)
    {
        if (member == null)
            return 0;

        if (Settings.BOT_ADMINS.stream().anyMatch(id -> id.equals(member.getId())))
            return 4;

        if (member.isOwner())
            return 3;

        if (member.hasPermission(Permission.ADMINISTRATOR))
            return 2;

        if (member.hasPermission(Permission.MESSAGE_MANAGE))
            return 1;

        return 0;
    }

    public static boolean check(int level, GuildMessageReceivedEvent event)
    {
        final Member member = event.getMember();
        if (member == null)
            return false;

        if (level > getLevel(member))
        {
            final EmbedBuilder eb = new EmbedBuilder()
                    .setColor(Color.red)
                    .setDescription("I'm sorry, but you do not have permissions to use this command!")
                    .setTimestamp(Instant.now());

            event.getChannel().sendMessage(eb.build()).queue();

            return true;
        }

        return false;
    }
}
