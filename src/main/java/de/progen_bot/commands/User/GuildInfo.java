package de.progen_bot.commands.User;

import java.awt.Color;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager.ParsedCommandString;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class GuildInfo extends CommandHandler {

	public GuildInfo() {
		super("guildinfo","guildinfo","get the guildinfo");
	}

	@Override
	public void execute(ParsedCommandString parsedCommand, MessageReceivedEvent event) {
		System.out.println("[Info] Command pb!userinfo wird ausgeführt!");
		Guild g = event.getGuild();

		List<Member> l = g.getMembers();

		String create = g.getCreationTime().format(DateTimeFormatter.RFC_1123_DATE_TIME);
		int bans = g.getBanList().complete().size();
		int Invite = g.getInvites().complete().size();
		String name = g.getName();
		String id = g.getId();
		String region = g.getRegion().getName();
		String avatar = g.getIconUrl();
		int textChans = g.getTextChannels().size();
		int voiceChans = g.getVoiceChannels().size();
		int rolesCount = g.getRoles().size();
		int afktime = g.getAfkTimeout().getSeconds() / 60;
		String afk = g.getAfkChannel() == null ? "Nicht eingestellt" : g.getAfkChannel().getName();
		Member owner = g.getOwner();

		int all = l.size();
		long users = l.stream().filter(m -> !m.getUser().isBot()).count();
		long onlineUsers = l.stream()
				.filter(m -> !m.getUser().isBot() && !m.getOnlineStatus().equals(OnlineStatus.OFFLINE)).count();
		long bots = l.stream().filter(m -> m.getUser().isBot()).count();
		long onlineBots = l.stream()
				.filter(m -> m.getUser().isBot() && !m.getOnlineStatus().equals(OnlineStatus.OFFLINE)).count();

		String roles = g.getRoles().stream().filter(r -> !r.getName().contains("everyone"))
				.map(r -> String.format("%s", r.getAsMention())).collect(Collectors.joining(", "));

		String usersText = String.format("**Members:**   %d   (Online:  %d)\n" + "**Bots:**   %d   (Online:  %d)",
				users, onlineUsers, bots, onlineBots);

		EmbedBuilder eb = new EmbedBuilder().setColor(Color.cyan).addField("Name:", name, false)
				.addField("ID:", "``" + id + "``", false)
				.addField("Inhaber:", owner.getUser().getName() + "#" + owner.getUser().getDiscriminator(), false)
				.addField("Server Region:", region, false)
				.addField("Channels:", "**TextChannels:**  " + textChans + "\n**VoiceChannels:**  " + voiceChans, false)
				.addField("Mitglieder (" + all + "):", usersText, false)
				.addField("Rollen (" + rolesCount + "): ", roles, false)
				.addField("Stats:",
						"**Erstellt am:**  " + create + "\n**Invites:  **" + Invite + "\n**Bans:**  " + bans
								+ "\n**AFK Timeout:**  " + afktime + " Minuten",
						false)
				.addField("AFK Channel:", afk, false);

		if (avatar != null)
			eb.setThumbnail(avatar);

		event.getTextChannel().sendMessage(eb.build()).queue();
	}

	@Override
	public String help() {
		return null;
	}
}
