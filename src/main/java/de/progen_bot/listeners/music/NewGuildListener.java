package de.progen_bot.listeners.music;

import de.progen_bot.core.Main;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

// TODO: 22.02.2021 proper name and package
public class NewGuildListener extends ListenerAdapter
{
    @Override
    public void onGuildJoin(@Nonnull GuildJoinEvent event)
    {
        Main.getMusicBotManager().loadForNewGuild(event.getGuild().getIdLong());
    }
}
