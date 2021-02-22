package de.progen_bot.listeners.events;

import de.progen_bot.misc.Logger;
import de.progen_bot.utils.statics.Statics;
import net.dv8tion.jda.api.events.ReconnectedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class ReconnectListener extends ListenerAdapter
{
    @Override
    public void onReconnected(@Nonnull ReconnectedEvent event)
    {
        Logger.info("Reconnected");

        Statics.increaseReconnectCount();
    }
}
