package de.progen_bot.core;

import com.mysql.cj.jdbc.Driver;
import de.progen_bot.api.API;
import de.progen_bot.core.music.MusicBotManager;
import de.progen_bot.database.DaoHandler;
import de.progen_bot.music.MusicManager;
import de.progen_bot.utils.statics.Settings;
import de.progen_bot.utils.topgg.TopGGIntegration;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import okhttp3.OkHttpClient;

import javax.security.auth.login.LoginException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

public class Main
{
    private static final String URL = String.format(
            "jdbc:mysql://%s:%s/%s?useUnicode=true&serverTimezone=UTC&autoReconnect=true",
            Settings.HOST,
            Settings.PORT,
            Settings.DATABASE
    );

    private static OkHttpClient client;
    private static JDA jda;
    private static Connection connection;
    private static DaoHandler daoHandler;
    private static TopGGIntegration topGGIntegration;
    private static MusicBotManager musicBotManager;
    private static MusicManager musicManager;
    private static API api;

    public Main()
    {
        Settings.loadSettings();

        try
        {
            DriverManager.registerDriver(new Driver());
            connection = DriverManager.getConnection(URL, Settings.USER, Settings.PASSWORD);
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Error connecting to database", e);
        }

        initJDA();

        client = new OkHttpClient();
        topGGIntegration = new TopGGIntegration(getJDA());
        daoHandler = new DaoHandler();
        musicBotManager = new MusicBotManager();
        musicManager = new MusicManager();
        api = new API(Integer.parseInt(Settings.API_PORT));

        // FIXME: 17.02.2021 replace with scheduler
        new Timer().schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                topGGIntegration.postServerCount();
            }
        }, 0, 30 * 60 * 1000);
    }

    private static void initJDA()
    {
        final JDABuilder builder = JDABuilder.createDefault(
                Settings.TOKEN,
                GatewayIntent.GUILD_BANS,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_VOICE_STATES,
                GatewayIntent.GUILD_PRESENCES,
                GatewayIntent.GUILD_MESSAGE_REACTIONS,
                GatewayIntent.DIRECT_MESSAGE_REACTIONS,
                GatewayIntent.DIRECT_MESSAGES)
                .enableCache(CacheFlag.ACTIVITY)
                .setMemberCachePolicy(MemberCachePolicy.ALL);


//        BuildManager.addEventListeners(builder);
        try
        {
            jda = builder.build().awaitReady();
        }
        catch (LoginException | InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public static JDA getJDA()
    {
        return jda;
    }

    public static Connection getConnection()
    {
        return connection;
    }

    public static OkHttpClient getClient()
    {
        return client;
    }

    public static DaoHandler getDaoHandler()
    {
        return daoHandler;
    }

    public static TopGGIntegration getTopGGIntegration()
    {
        return topGGIntegration;
    }

    public static MusicBotManager getMusicBotManager()
    {
        return musicBotManager;
    }

    public static MusicManager getMusicManager()
    {
        return musicManager;
    }

    public static API getAPI()
    {
        return api;
    }

    public static void main(String[] args)
    {
        new Main();
    }
}
