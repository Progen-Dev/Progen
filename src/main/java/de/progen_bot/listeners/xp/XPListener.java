package de.progen_bot.listeners.xp;

import de.progen_bot.database.dao.config.ConfigDaoImpl;
import de.progen_bot.database.entities.xp.UserData;
import de.progen_bot.utils.statics.Settings;
import de.progen_bot.utils.statics.Statics;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class XPListener extends ListenerAdapter
{
    private final List<String> spamFilter = new ArrayList<>();

    /**
     * On guild message received.
     *
     * @param event the event
     */
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.getMessage().getContentRaw().startsWith(Settings.PREFIX) || event.getMessage().getContentRaw().startsWith(new ConfigDaoImpl().loadConfig(event.getGuild().getIdLong()).getPrefix()) || spamFilter.contains(event.getAuthor().getId())) {
            return;
        }

        UserData data = UserData.fromId(event.getAuthor().getId());

        if (data == null) {
            data = new UserData();
            data.setUserId(event.getAuthor().getId());
        }

        data.setTotalXp(data.getTotalXp() + Statics.XP_GAIN);
        UserData.save(data);

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
