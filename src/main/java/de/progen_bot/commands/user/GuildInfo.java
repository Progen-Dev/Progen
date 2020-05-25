package de.progen_bot.commands.user;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager.ParsedCommandString;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.permissions.AccessLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class GuildInfo extends CommandHandler {

    public GuildInfo() {
        super("guildinfo", "guildinfo", "get the guildinfo");
    }

    @Override
    public void execute(ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {
        Guild g = event.getGuild();

        List<Member> l = g.getMembers();

        String create = g.getTimeCreated().format(DateTimeFormatter.RFC_1123_DATE_TIME);
        int bans = g.retrieveBanList().complete().size();
        int Invite = g.retrieveInvites().complete().size();
        String name = g.getName();
        String id = g.getId();
        String region = g.getRegion().getName();
        String avatar = g.getIconUrl();
        int textChans = g.getTextChannels().size();
        int voiceChans = g.getVoiceChannels().size();
        int rolesCount = g.getRoles().size();
        int afktime = g.getAfkTimeout().getSeconds() / 60;
        String afk = g.getAfkChannel() == null ? "Not set" : g.getAfkChannel().getName();
        Member owner = g.getOwner();

        int all = l.size();
        long users = l.stream().filter(m -> !m.getUser().isBot()).count();
        long onlineUsers = l.stream()
                .filter(m -> !m.getUser().isBot() && !m.getOnlineStatus().equals(OnlineStatus.ONLINE)).count();
        long bots = l.stream().filter(m -> m.getUser().isBot()).count();
        long onlineBots = l.stream()
                .filter(m -> m.getUser().isBot() && !m.getOnlineStatus().equals(OnlineStatus.ONLINE)).count();

        String roles = g.getRoles().stream().filter(r -> !r.getName().contains("everyone"))
                .map(r -> String.format("%s", r.getAsMention())).collect(Collectors.joining(", "));

        String usersText = String.format("**Members:**   %d   (Online:  %d)\n" + "**Bots:**   %d   (Online:  %d)",
                users, onlineUsers, bots, onlineBots);

        if (owner == null)
            return;

        EmbedBuilder eb = new EmbedBuilder().setColor(Color.cyan).addField("Name:", name, false)
                .addField("ID:", "``" + id + "``", false)
                .addField("Owner:", owner.getUser().getName() + "#" + owner.getUser().getDiscriminator(), false)
                .addField("Server Region:", region, false)
                .addField("Channels:", "**TextChannels:**  " + textChans + "\n**VoiceChannels:**  " + voiceChans, false)
                .addField("Members (" + all + "):", usersText, false)
                .addField("Roles (" + rolesCount + "): ", roles, false)
                .addField("Stats:",
                        "**Created on:**  " + create + "\n**Invites:  **" + Invite + "\n**Bans:**  " + bans
                                + "\n**AFK Timeout:**  " + afktime + " minutes",
                        false)
                .addField("AFK Channel:", afk, false);

        if (avatar != null)
            eb.setThumbnail(avatar);

        event.getTextChannel().sendMessage(eb.build()).queue();
    }

    @Override
    public AccessLevel getAccessLevel() {
        return AccessLevel.USER;
    }
}
