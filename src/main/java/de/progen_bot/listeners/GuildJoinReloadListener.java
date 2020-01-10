package de.progen_bot.listeners;

import de.progen_bot.core.Main;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class GuildJoinReloadListener extends ListenerAdapter {

    @Override
    public void onGuildJoin(@Nonnull GuildJoinEvent event) {

        Main.getMusicBotManager().loadForNewGuild(event.getGuild());
    }
}
