package de.progen_bot.listeners;

import de.progen_bot.db.entities.PollData;
import de.progen_bot.util.Util;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class VotingListener extends ListenerAdapter {

    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        if (event.getMember().getUser().isBot()) {
            return;
        }
        String emoteName = event.getReactionEmote().getName();

        PollData pData = new PollData().getDbData(event.getMessageId());

        if (pData == null) {
            return;
        }

        if (!pData.isOpen() || pData.getMessageId() == null) {
            return;
        }

        event.getReaction().removeReaction(event.getUser()).queue();
        List<String> usersVoted;
        if (pData.getUsers() != null) {
            usersVoted = new ArrayList<>(Arrays.asList(pData.getUsers().split(",")));
        } else {
            usersVoted = new ArrayList<>(Collections.emptyList());
        }

        if (usersVoted.contains(event.getUser().getId())) {
            return;
        }

        pData.setOptions(Util.getOptionFromMessage(pData, emoteName));
        usersVoted.add(event.getUser().getId());

        StringBuilder users = new StringBuilder();
        for (String tmp : usersVoted) {
            users.append(tmp).append(",");
        }

        pData.setUsers(users.toString());
        pData.saveToDb(pData);
    }
}
