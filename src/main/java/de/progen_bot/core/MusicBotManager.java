package de.progen_bot.core;

import de.progen_bot.util.Settings;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;

import javax.security.auth.login.LoginException;
import java.util.*;

public class MusicBotManager {

    private HashMap<Guild, List<JDA>> botsNotInUse = new HashMap<>();
    private List<String> tokens = new ArrayList<>();

    public MusicBotManager() {

        tokens.add(Settings.MUSICTOKEN1);
        tokens.add(Settings.MUSICTOKEN2);

        //Progen
        for (Guild g : Main.getJda().getGuilds()) {
            if (!botsNotInUse.containsKey(g)) botsNotInUse.put(g, new ArrayList<>());
            botsNotInUse.get(g).add(Main.getJda());
        }

        // Music bots
        for (String token : tokens) {
            JDABuilder builder = new JDABuilder(AccountType.BOT).setToken(token);
            builder.setAutoReconnect(true);
            try {
                JDA jda = builder.build().awaitReady();
                for (Guild g : jda.getGuilds()) {
                    if (!botsNotInUse.containsKey(g)) botsNotInUse.put(g, new ArrayList<>());
                    botsNotInUse.get(g).add(jda);
                }
            } catch (LoginException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public JDA getUnusedBot(Guild guild) {
        if (!botsNotInUse.containsKey(guild)) return null;
        if (botsNotInUse.get(guild).isEmpty()) return null;
        JDA bot = botsNotInUse.get(guild).get(0);
        botsNotInUse.get(guild).remove(0);
        return bot;
    }

    public boolean botAvailable(Guild guild) {
        if (botsNotInUse.get(guild).isEmpty()) return false;
        else return true;
    }

    public void setBotUnsed(Guild guild, JDA bot) {
        botsNotInUse.get(guild).add(bot);
    }
}
