package de.progen_bot.listeners;

import de.progen_bot.util.Statics;
import net.dv8tion.jda.api.events.ReconnectedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ReconnectListener extends ListenerAdapter {
    @Override
    public void onReconnect(ReconnectedEvent event){
        System.out.println("[INFO] Reconnect");

        Statics.reconnectCount++;
    }

}
