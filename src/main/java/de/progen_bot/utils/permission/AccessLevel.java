package de.progen_bot.utils.permission;

import de.progen_bot.utils.statics.Settings;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public enum AccessLevel
{
    USER(0),
    TRUSTED(1),
    MODERATOR(2),
    ADMINISTRATOR(3),
    OWNER(4),
    BOT_OWNER(5);

    private final int level;

    AccessLevel(int level)
    {
        this.level = level;
    }

    public static AccessLevel parse(int level)
    {
        for (AccessLevel accessLevel : AccessLevel.values())
        {
            if (level == accessLevel.getLevel())
                return accessLevel;
        }

        return null;
    }

    public static AccessLevel getAccessLevel(Member member)
    {
        if (member == null)
            return AccessLevel.USER;
        if (Settings.BOT_OWNERS.stream().anyMatch(id -> id.equals(member.getId())))
            return AccessLevel.BOT_OWNER;

        if (member.isOwner())
            return AccessLevel.OWNER;

        if (member.hasPermission(Permission.ADMINISTRATOR))
            return AccessLevel.ADMINISTRATOR;

        if (member.hasPermission(Permission.MANAGE_CHANNEL))
            return AccessLevel.MODERATOR;

        /* add AccessLevel.TRUSTED */

        return AccessLevel.USER;
    }

    /**
     * Checks whether a member is allowed to execute a command or not
     *
     * @param level {@link de.progen_bot.utils.permission.AccessLevel#getLevel() AccessLevel#getLevel()} of member
     * @param event {@link net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent GuildMessageReceivedEvent}
     * @return <code>true</code> if member is allowed to use the command, otherwise <code>false</code>
     */
    public static boolean isAllowed(int level, GuildMessageReceivedEvent event)
    {
        Member member = event.getMember();

        if (member == null)
            // FIXME: 17.02.2021 find async way
            member = event.getGuild().retrieveMember(event.getAuthor()).complete();

        if (level > getAccessLevel(member).getLevel())
        {
            event.getChannel().sendMessage(
                    new EmbedBuilder().setColor(Color.red).setDescription("I'm sorry, bot you don't have permission to use this command!").build()
            ).queue(m -> m.delete().queueAfter(3, TimeUnit.SECONDS));

            return false;
        }

        return true;
    }

    public int getLevel()
    {
        return level;
    }
}
