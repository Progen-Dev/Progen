package de.progen_bot.listeners.events;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class ReadyListener extends ListenerAdapter
{
    private final ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();

    @Override
    public void onReady(@Nonnull ReadyEvent event)
    {
        final StringBuilder sb = new StringBuilder(String.format("Progen is running on: %s guild(s) ----------------------------------%n", event.getGuildTotalCount()));

        for (Guild g : event.getJDA().getGuildCache())
            sb.append('-').append(' ').append(g.getName()).append(' ').append('(').append(g.getId()).append(')').append('\n');

        System.out.println(sb.toString());

        final Activity[] games = new Activity[] {
                Activity.playing("on " + event.getGuildTotalCount() + " guild(s)"),
                Activity.playing("pb!help | https://pwi.progen-bot.de")
        };

        ses.scheduleWithFixedDelay(() ->
        {
            games[0] = Activity.playing("on " + event.getGuildTotalCount() + " guilds");

            event.getJDA().getPresence().setActivity(games[ThreadLocalRandom.current().nextInt(games.length)]);
        }, 0, 30*60*1000, TimeUnit.MILLISECONDS);
    }
}
