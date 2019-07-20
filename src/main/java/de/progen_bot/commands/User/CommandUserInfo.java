package de.progen_bot.commands.User;

import java.awt.Color;
import java.time.format.DateTimeFormatter;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager.ParsedCommandString;
import de.progen_bot.core.PermissionCore;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandUserInfo extends CommandHandler {

	public CommandUserInfo() {
		super("userinfo","userinfo <user>","get userinfos");
	}

	@Override
	public void execute(ParsedCommandString parsedCommand, MessageReceivedEvent event) {
		System.out.println("[Info] Command pb!userinfo wird ausgeführt!");
		Member memb;

		if (event.getMessage().getMentionedUsers().size() == 1) {
			memb = event.getGuild().getMember(event.getMessage().getMentionedUsers().get(0));
		} else {
			memb = event.getMember();
		}

		String NAME = memb.getEffectiveName();
		String TAG = memb.getUser().getName() + "#" + memb.getUser().getDiscriminator();
		String GUILD_JOIN_DATE = memb.getJoinDate().format(DateTimeFormatter.RFC_1123_DATE_TIME);
		String DISCORD_JOINED_DATE = memb.getUser().getCreationTime().format(DateTimeFormatter.RFC_1123_DATE_TIME);
		String ID = memb.getUser().getId();
		String STATUS = memb.getOnlineStatus().name();
		String ROLES = "";
		String GAME;
		String AVATAR = memb.getUser().getAvatarUrl();
		String PERM = PermissionCore.getLevel(memb) + "";

		try {
			GAME = memb.getGame().getName();
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
