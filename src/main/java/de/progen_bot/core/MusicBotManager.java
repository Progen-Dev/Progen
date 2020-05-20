package de.progen_bot.core;

import de.progen_bot.listeners.GuildJoinReloadListener;
import de.progen_bot.util.Settings;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;

import javax.security.auth.login.LoginException;
import java.util.*;

public class MusicBotManager {

    private HashMap<String, List<JDA>> botIDsNotInUse = new HashMap<>();
    private List<String> tokens = Settings.MUSIC;
    private List<JDA> allMusicBots = new ArrayList<>();

    public MusicBotManager() {

        //Progen
        for (Guild g : Main.getJda().getGuilds()) {
            if (!botIDsNotInUse.containsKey(g.getId())) botIDsNotInUse.put(g.getId(), new ArrayList<>());
            botIDsNotInUse.get(g.getId()).add(Main.getJda());
        }

        if (tokens.isEmpty())
            return;

        // Music bots
        for (String token : tokens) {
            JDABuilder builder = new JDABuilder(AccountType.BOT).setToken(token);
            builder.setAutoReconnect(true);
            builder.addEventListeners(new GuildJoinReloadListener());
            try {
                JDA jda = builder.build().awaitReady();
                allMusicBots.add(jda);
                for (Guild g : jda.getGuilds()) {
                    if (!botIDsNotInUse.containsKey(g.getId())) botIDsNotInUse.put(g.getId(), new ArrayList<>());
                    botIDsNotInUse.get(g.getId()).add(jda);
                }
            } catch (LoginException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public JDA getUnusedBot(Guild guild) {
        if (!botIDsNotInUse.containsKey(guild.getId())) return null;
        if (botIDsNotInUse.get(guild.getId()).isEmpty()) return null;
        JDA bot = botIDsNotInUse.get(guild.getId()).get(0);
        botIDsNotInUse.get(guild.getId()).remove(0);
        return bot;
    }

    public boolean botAvailable(Guild guild) {
        if (botIDsNotInUse.get(guild.getId()).isEmpty()) return false;
        else return true;
    }

    public void loadForNewGuild(Guild guild) {

        // Clear the list in case of reinvite
        botIDsNotInUse.remove(guild.getId());
        botIDsNotInUse.put(guild.getId(), new ArrayList<>());

        // Add Progen
        botIDsNotInUse.get(guild.getId()).add(Main.getJda());

        // Add music bpots
        for (JDA bot : allMusicBots) {

            if (bot.getGuilds().contains(guild)) botIDsNotInUse.get(guild.getId()).add(bot);
        }
    }

    public void setBotUnsed(Guild guild, JDA bot) {
        botIDsNotInUse.get(guild.getId()).add(bot);
    }
}
