package de.progen_bot.utils.topgg;

import net.dv8tion.jda.api.JDA;
import org.discordbots.api.client.DiscordBotListAPI;

import de.progen_bot.utils.statics.Settings;

import java.util.function.Consumer;

public class TopGGIntegration
{
    private final JDA jda;
    private final DiscordBotListAPI api;

    /**
     * Creates a new integration instance for entered {@link net.dv8tion.jda.api.JDA JDA} instance.
     *
     * @param jda {@link net.dv8tion.jda.api.JDA JDA} of running bot
     */
    public TopGGIntegration(JDA jda)
    {
        this.jda = jda;
        this.api = new DiscordBotListAPI.Builder()
                .token(Settings.TOP_GG_TOKEN)
                .botId(jda.getSelfUser().getId())
                .build();
    }

    /**
     * Update guild count on top.gg
     */
    public void postServerCount()
    {
        // FIXME: 16.02.2021 may not be cached
        this.api.setStats(this.jda.getGuilds().size());
    }

    /**
     * Check whether a user has voted for the bot or not
     *
     * @param userId id of User to check
     * @param callback {@link java.util.function.Consumer<Boolean> Consumer<Boolean>} for asynchronous usage
     */
    public void hasVoted(long userId, Consumer<Boolean> callback)
    {
        this.api.hasVoted(String.valueOf(userId)).whenComplete((voted, throwable) ->
        {
            if (throwable != null)
            {
                // TODO: 16.02.2021 debug output
                throwable.printStackTrace();
                callback.accept(false);
            }

            callback.accept(voted);
        });
    }

    /**
     * Get instance of top.gg API interface
     *
     * @return {@link org.discordbots.api.client.DiscordBotListAPI DiscordBotListAPI}
     */
    public DiscordBotListAPI getAPI()
    {
        return api;
    }
}
