package de.progen_bot.core;

import de.progen_bot.util.Settings;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import org.discordbots.api.client.DiscordBotListAPI;

import java.util.function.Consumer;

public class TopGGIntegration {

    private final DiscordBotListAPI api;
    private final JDA jda;

    public TopGGIntegration(JDA jda) {
        this.jda = jda;
        api = new DiscordBotListAPI.Builder()
                .token(Settings.TOP_GG_TOKEN)
                .botId(jda.getSelfUser().getId())
                .build();
    }

    public void postServerCount() {
        api.setStats(jda.getGuilds().size());
    }

    public void hasVoted(User user, Consumer<Boolean> callback) {
        api.hasVoted(user.getId()).whenComplete((hasVoted, e) -> {
            if (hasVoted == null) {
                try {
                    throw e;
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
            callback.accept(hasVoted);
        });
    }
}
