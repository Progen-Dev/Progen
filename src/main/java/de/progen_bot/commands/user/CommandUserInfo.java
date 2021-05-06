package de.progen_bot.commands.user;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager.ParsedCommandString;
import de.progen_bot.permissions.AccessLevel;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.permissions.PermissionCore;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CommandUserInfo extends CommandHandler {

    public CommandUserInfo() {
        super("userinfo", "userinfo <user>", "get userinfos");
    }

    @Override
    public void execute(ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {
        Member member;

        if (event.getMessage().getMentionedMembers().size() == 1) {
            member = event.getMessage().getMentionedMembers().get(0);
        } else {
            member = event.getMember();
        }

        if (member == null)
            return;


        final String name = member.getEffectiveName();
        final String nickname = member.getNickname();
        final String onGuildJoinDate = member.getTimeJoined().format(DateTimeFormatter.RFC_1123_DATE_TIME);
        final String booster = member.getTimeBoosted().format(DateTimeFormatter.RFC_1123_DATE_TIME);

        event.getTextChannel().sendMessage(
                name
        ).queue();
    }

    @Override
    public AccessLevel getAccessLevel() {
        return AccessLevel.USER;
    }

}
