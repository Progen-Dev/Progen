package de.progen_bot.listeners;

import de.progen_bot.core.Main;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {

    boolean setChannelMsg = false;
    boolean setMessage = false;

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        if (Main.getMessage().contains(event.getAuthor().getIdLong())){
            if (!setChannelMsg){
                event.getChannel().sendMessage("Please enter a valid Textchannel").queue();
                setChannelMsg = true;
                return;
            }

            if (!setMessage && setChannelMsg){
                event.getChannel().sendMessage("They now write a text.").queue();
                setMessage = true;
                return;
            }

            if (setChannelMsg && setMessage){
                event.getChannel().sendMessage("Successfully setuped").queue();
                setMessage = false;
                setChannelMsg = false;
                Main.getMessage().remove(event.getAuthor().getIdLong());
            }
        }
    }
}
