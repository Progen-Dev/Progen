package de.progen_bot.listeners;

import de.progen_bot.commands.Settings.CommandVote;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ReactionListener extends ListenerAdapter {

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event){
        CommandVote.handleReaction(event);
    }
}
