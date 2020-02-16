package de.progen_bot.listeners;

import de.progen_bot.commands.Settings.CommandVote;
import de.progen_bot.util.Statics;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The listener interface for receiving ready events.
 * The class that is interested in processing a ready
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addReadyListener<code> method. When
 * the ready event occurs, that object's appropriate
 * method is invoked.
 *
 * @see ReadyEvent
 */
public class ReadyListener extends ListenerAdapter {

    /* (non-Javadoc)
     * @see net.dv8tion.jda.de.progen_bot.core.hooks.ListenerAdapter#onReady(net.dv8tion.jda.de.progen_bot.core.events.ReadyEvent)
     */
    public void onReady(ReadyEvent event) {

        String out = "\nProgen is running on:\n" + "----------------------------------\n";

        for (Guild g : event.getJDA().getGuilds()) {
            out += "-" + g.getName() + "(" + g.getId() + ")" + "\n";
        }

        Activity[] games = new Activity[]{
                Activity.playing("Hello!"),
                Activity.watching("https://progen-bot.de/"),
                Activity.streaming("Check the Webinterface","https://pwi.progen-bot.de/"),
                Activity.playing("https://pwi.progen-bot.de!"),
                Activity.playing(Statics.Version)};

        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                event.getJDA().getPresence().setActivity(games[ThreadLocalRandom.current().nextInt(5)]);
            }

        }, 0, 10000);

        System.out.println(out);

        //CommandPoll
        CommandVote.loadPolls(event.getJDA());
    }
}