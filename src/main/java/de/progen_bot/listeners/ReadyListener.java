package de.progen_bot.listeners;

import de.progen_bot.commands.settings.CommandVote;
import de.progen_bot.util.Statics;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The listener interface for receiving ready events. The class that is
 * interested in processing a ready event implements this interface, and the
 * object created with that class is registered with a component using the
 * component's <code>addReadyListener<code> method. When the ready event occurs,
 * that object's appropriate method is invoked.
 *
 * @see ReadyEvent
 */
public class ReadyListener extends ListenerAdapter {
    /*
     * (non-Javadoc)
     * 
     * @see
     * net.dv8tion.jda.api.hooks.ListenerAdapter#onReady(net.dv8tion.jda.api.events.
     * ReadyEvent)
     */
    @Override
    public void onReady(ReadyEvent event) {
        StringBuilder out = new StringBuilder("\nProgen is running on: " + event.getGuildTotalCount() + " guilds "
                + "----------------------------------\n");

        for (Guild g : event.getJDA().getGuilds()) {
            out.append("-").append(g.getName()).append("(").append(g.getId()).append(")").append("\n");
        }

        Activity[] games = new Activity[] { Activity.playing("Runs on x  guilds"),
                Activity.playing("pb!help | pwi.progen-bot.de"),
                Activity.playing(Statics.VERSION) };

        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                games[0] = Activity.playing("Runs on: " + event.getGuildTotalCount() + " guilds");

                event.getJDA().getPresence().setActivity(games[ThreadLocalRandom.current().nextInt(games.length)]);
            }

        }, 0, 10000);

        System.out.println(out.toString());

        // CommandPoll
        CommandVote.loadPolls(event.getJDA());

        Statics.setLastRestart(new Date());
    }
}