package de.progen_bot.listeners;

import de.progen_bot.util.Statics;
import net.dv8tion.jda.api.events.ReconnectedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ReconnectListener extends ListenerAdapter {
    @Override
    public void onReconnect(@NotNull ReconnectedEvent event){
        System.out.println("[INFO] Reconnect");

        Statics.increaseReconnectCount();
    }

}
