package de.progen_bot.commands.user;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager.ParsedCommandString;
import de.progen_bot.permissions.AccessLevel;
import de.progen_bot.permissions.PermissionCore;
import de.progen_bot.db.entities.config.GuildConfiguration;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.time.format.DateTimeFormatter;

public class CommandUserInfo extends CommandHandler {

    public CommandUserInfo() {
        super("userinfo" , "userinfo <user>" , "get userinfos");
    }

    @Override
    public void execute(ParsedCommandString parsedCommand , MessageReceivedEvent event , GuildConfiguration configuration) {
        Member memb;

        if (event.getMessage().getMentionedMembers().size() == 1) {
            memb = event.getMessage().getMentionedMembers().get(0);
        } else {
            memb = event.getMember();
        }

        if (memb == null)
            return;

        final String name = memb.getEffectiveName();
        final String tag = memb.getUser().getAsTag();
        final String guildJoinDate = memb.getTimeJoined().format(DateTimeFormatter.RFC_1123_DATE_TIME);
        final String discordJoinedDate = memb.getUser().getTimeCreated().format(DateTimeFormatter.RFC_1123_DATE_TIME);
        final String id = memb.getUser().getId();
        final String status = memb.getOnlineStatus().name();
        final String avatarUrl = memb.getUser().getEffectiveAvatarUrl();
        StringBuilder roles = new StringBuilder();
        String game;

        try {
            game = memb.getActivities().get(0).getName();
        } catch (Exception e) {
            game = "-/-";
        }

        for (Role r : memb.getRoles()) {
            roles.append(r.getAsMention()).append(", ");
        }
        if (roles.length() > 0)
            roles = new StringBuilder(roles.substring(0 , roles.length() - 2));
        else
            roles = new StringBuilder("No role on this server");

        EmbedBuilder em = new EmbedBuilder().setColor(Color.green);
        if (memb.getUser().isBot()) {
            em.setDescription("**Bot Information for " + memb.getUser().getName() + ":**");
        } else
            em.setDescription("**User Information for " + memb.getUser().getName() + ":**");

        em.addField("Name / Nickname" , name , false)
                .addField("User Tag" , tag , false)
                .addField("id" , id , false)
                .addField("Current Status" , status , false)
                .addField("Current Game" , game , false)
                .addField("Roles" , roles.toString() , false)
                .addField("Server joined" , guildJoinDate , false)
                .addField("Discord joined" , discordJoinedDate , false)
                .setThumbnail(avatarUrl);

        event.getTextChannel().sendMessage(em.build()).queue();

    }

    @Override
    public AccessLevel getAccessLevel() {
        return AccessLevel.USER;
    }
}