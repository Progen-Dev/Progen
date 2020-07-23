package de.progen_bot.progen.core;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.discordbots.api.client.DiscordBotListAPI;

import javax.security.auth.login.LoginException;
import java.util.function.Consumer;

/**
 * Main class of Progen
 *
 * @author Progen-Dev
 * @since 2.0.0
 */
public class Main
{
    private static Main instance;

    private final JDA jda;
    private final TopGGIntegration topGGIntegration;

    /**
     * Start Progen
     *
     * @throws LoginException {@link LoginException LoginException}
     * @throws InterruptedException {@link InterruptedException InterruptedException}
     */
    private Main() throws LoginException, InterruptedException
    {
        instance = this;

        this.jda = JDABuilder.createDefault("token").build().awaitReady();
        this.topGGIntegration = new TopGGIntegration(this.getJDA());
        this.topGGIntegration.postGuildStats();
    }

    public static void main(String[] args) throws LoginException, InterruptedException
    {
        new Main();
    }

    public static Main getInstance()
    {
        return instance;
    }

    public JDA getJDA()
    {
        return jda;
    }

    public TopGGIntegration getTopGGIntegration()
    {
        return topGGIntegration;
    }

    private static final class TopGGIntegration
    {
        private final JDA jda;
        private final DiscordBotListAPI botListAPI;

        public TopGGIntegration(JDA jda)
        {
            this.jda = Main.getInstance().getJDA();
            this.botListAPI = new DiscordBotListAPI.Builder()
                    .token("token")
                    .botId(jda.getSelfUser().getId())
                    .build();
        }

        public void postGuildStats()
        {
            botListAPI.setStats(jda.getGuilds().size());
        }

        public void hasVoted(String userId, Consumer<Boolean> consumer)
        {
            botListAPI.hasVoted(userId).whenComplete((voted, e) ->
            {
                if (!voted)
                {
                    try
                    {
                        throw e;
                    }
                    catch (Throwable throwable)
                    {
                        throwable.printStackTrace();
                    }
                }

                consumer.accept(voted);
            });
        }
    }
}
