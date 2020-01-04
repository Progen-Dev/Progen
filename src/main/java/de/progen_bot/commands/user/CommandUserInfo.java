package de.progen_bot.commands.user;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager.ParsedCommandString;
import de.progen_bot.core.PermissionCore;
import de.progen_bot.db.entities.config.GuildConfiguration;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.time.format.DateTimeFormatter;

public class CommandUserInfo extends CommandHandler {

    public CommandUserInfo() {
        super("userinfo", "userinfo <user>", "get userinfos");
    }

    @Override
    public void execute(ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {
        Member memb;

        if (event.getMessage().getMentionedUsers().size() == 1) {
            memb = event.getGuild().getMember(event.getMessage().getMentionedUsers().get(0));
        } else {
            memb = event.getMember();
        }

        String NAME = memb.getEffectiveName();
        String TAG = memb.getUser().getName() + "#" + memb.getUser().getDiscriminator();
        String GUILD_JOIN_DATE = memb.getTimeJoined().format(DateTimeFormatter.RFC_1123_DATE_TIME);
        String DISCORD_JOINED_DATE = memb.getUser().getTimeCreated().format(DateTimeFormatter.RFC_1123_DATE_TIME);
        String ID = memb.getUser().getId();
        String STATUS = memb.getOnlineStatus().name();
        String ROLES = "";
        String GAME;
        String AVATAR = memb.getUser().getAvatarUrl();
        String PERM = PermissionCore.getLevel(memb) + "";

        try {
            GAME = memb.getActivities().get(0).getName();
        } catch (Exception e) {
            GAME = "-/-";
        }

        for (Role r : memb.getRoles()) {
            ROLES += r.getAsMention() + ", ";
        }
        if (ROLES.length() > 0)
            ROLES = ROLES.substring(0, ROLES.length() - 2);
        else
            ROLES = "Keine Rollen auf diesem Server";

        if (AVATAR == null) {
            AVATAR = "Kein Avatar";
        }

        EmbedBuilder em = new EmbedBuilder().setColor(Color.green);
        if (memb.getUser().isBot()) {
            em.setDescription("**Bot Informationen für " + memb.getUser().getName() + ":**");
        } else
            em.setDescription("**User Informationen für " + memb.getUser().getName() + ":**");

        em.addField("Name / Nickname", NAME, false);
        em.addField("User Tag", TAG, false);
        em.addField("ID", ID, false);
        em.addField("Aktueller Status", STATUS, false);
        em.addField("Aktuelles Spiel", GAME, false);
        em.addField("Rollen", ROLES, false);
        em.addField("Guildberechtigungsstufe", "``" + PERM + "``", false);
        em.addField("Server beigetreten", GUILD_JOIN_DATE, false);
        em.addField("Discord beigetreten", DISCORD_JOINED_DATE, false);

        if (AVATAR != "Kein Avatar") {
            em.setThumbnail(AVATAR);
        }

        event.getTextChannel().sendMessage(em.build()).queue();

    }

    @Override
    public String help() {
        return null;
    }

}
