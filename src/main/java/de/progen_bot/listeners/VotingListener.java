package de.progen_bot.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.progen_bot.db.PollData;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import de.progen_bot.util.Util;

public class VotingListener extends ListenerAdapter {

	public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
		if (event.getMember().getUser().isBot()) {
			return;
		}
		String emoteName = event.getReactionEmote().getName();
		
		PollData pData = new PollData().getDbData(event.getMessageId());
		
		if(pData == null) {
			return;
		}
		
		if (!pData.isOpen() || pData.getMessageId() == null) {
			return;
		}
		
		event.getReaction().removeReaction(event.getUser()).queue();
		List<String> usersVoted = null;
		if (pData.getUsers() != null) {
			usersVoted = new ArrayList<String>(Arrays.asList(pData.getUsers().split(",")));
		} else {
			usersVoted = new ArrayList<String>(Arrays.asList());
		}

		if (usersVoted.contains(event.getUser().getId())) {
			return;
		}

		pData.setOptions(Util.getOptionFromMessage(pData,emoteName));
		usersVoted.add(event.getUser().getId());

		StringBuilder users = new StringBuilder();
		for (String tmp : usersVoted) {
			users.append(tmp + ",");
		}

		pData.setUsers(users.toString());
		pData.saveToDb(pData);
	}
}
