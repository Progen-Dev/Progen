package de.progen_bot.listeners;

import de.progen_bot.db.entities.UserData;
import de.progen_bot.util.Settings;
import de.progen_bot.util.Statics;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The listener interface for receiving xp events. The class that is interested
 * in processing a xp event implements this interface, and the object created
 * with that class is registered with a component using the component's
 * <code>addXpListener<code> method. When the xp event occurs, that object's
 * appropriate method is invoked.
 */
public class XPListener extends ListenerAdapter {
    private List<String> spamFilter = new ArrayList<>();

    /**
     * On guild message received.
     *
     * @param event the event
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.getMessage().getContentRaw().startsWith(Settings.PREFIX) || spamFilter.contains(event.getAuthor().getId())) {
            return;
        }

        UserData data = UserData.fromId(event.getAuthor().getId());

        if (data == null) {
            data = new UserData();
            data.setUserId(event.getAuthor().getId());
        }

        data.setTotalXp(data.getTotalXp() + Statics.XP_GAIN);
        data.save(data);

        spamFilter.add(event.getAuthor().getId());
        //only 1 Message every 60s counts
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                spamFilter.remove(event.getAuthor().getId());
            }
        }, 1000 * 10);
    }
}
