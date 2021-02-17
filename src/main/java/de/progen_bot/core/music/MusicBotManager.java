package de.progen_bot.core.music;

import de.progen_bot.core.Main;
import de.progen_bot.utils.statics.Settings;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.ISnowflake;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MusicBotManager
{
    private final HashMap<Long, List<JDA>> botIDsNotInUse = new HashMap<>();
    private final List<JDA> allMusicBots = new ArrayList<>();

    public MusicBotManager()
    {

        //Progen
        for (Guild g : Main.getJDA().getGuilds())
        {
            if (!botIDsNotInUse.containsKey(g.getIdLong()))
                botIDsNotInUse.put(g.getIdLong(), new ArrayList<>());

            botIDsNotInUse.get(g.getIdLong()).add(Main.getJDA());
        }

        List<String> tokens = Settings.MUSIC;
        if (tokens.isEmpty())
            return;

        // Music bots
        for (String token : tokens)
        {
            final JDABuilder builder = JDABuilder.createDefault(token);
            builder.setAutoReconnect(true);
//            builder.addEventListeners(new GuildJoinReloadListener());
            try
            {
                final JDA jda = builder.build().awaitReady();
                allMusicBots.add(jda);
                for (Guild g : jda.getGuilds())
                {
                    if (!botIDsNotInUse.containsKey(g.getIdLong()))
                        botIDsNotInUse.put(g.getIdLong(), new ArrayList<>());

                    botIDsNotInUse.get(g.getIdLong()).add(jda);
                }
            }
            catch (LoginException | InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    public JDA getUnusedBot(long guildId)
    {
        if (!botIDsNotInUse.containsKey(guildId))
            return null;
        if (botIDsNotInUse.get(guildId).isEmpty())
            return null;

        final JDA bot = botIDsNotInUse.get(guildId).get(0);
        botIDsNotInUse.get(guildId).remove(0);

        return bot;
    }

    public boolean botAvailable(long guildId)
    {
        return !botIDsNotInUse.get(guildId).isEmpty();
    }

    public void loadForNewGuild(long guildId)
    {

        // Clear the list in case of reinvite
        botIDsNotInUse.remove(guildId);
        botIDsNotInUse.put(guildId, new ArrayList<>());

        // Add Progen
        botIDsNotInUse.get(guildId).add(Main.getJDA());

        // Add music bots
        for (JDA bot : allMusicBots)
        {

            if (bot.getGuilds().stream().map(ISnowflake::getIdLong).anyMatch(id -> id == guildId))
                botIDsNotInUse.get(guildId).add(bot);
        }
    }

    public void setBotUnused(long guildId, JDA bot)
    {
        botIDsNotInUse.get(guildId).add(bot);
    }
}
