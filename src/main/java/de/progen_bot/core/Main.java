package de.progen_bot.core;

import com.mysql.cj.jdbc.Driver;

import de.progen_bot.commands.administrator.ChangePrefix;
import de.pwi.api.httpapi.API;
import de.progen_bot.command.CommandManager;
import de.progen_bot.commands.moderator.*;
import de.progen_bot.commands.moderator.blacklist.*;
import de.progen_bot.commands.music.*;
import de.progen_bot.commands.owner.*;
import de.progen_bot.commands.settings.*;
import de.progen_bot.commands.user.*;
import de.progen_bot.commands.xp.*;
import de.progen_bot.commands.fun.*;
import de.progen_bot.commands.*;
import de.progen_bot.db.DaoHandler;
import de.progen_bot.music.MusicManager;
import de.progen_bot.util.Settings;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;


/**
 * The Class Main.
 */
public class Main {

    private static final String URL = "jdbc:mysql://" + Settings.HOST + ":" + Settings.PORT + "/" +
            Settings.DATABASE + "?useUnicode=true&serverTimezone=UTC&autoReconnect=true";

    private static JDA jda;

    private static Connection sqlConnection;

    private static CommandManager commandManager;

    private static DaoHandler daoHandler;

    private static MusicBotManager musicBotManager;
    private static MusicManager musicManager;

    private static TopGGIntegration topGGIntegration;

    /**
     * Instantiates a new main.
     */
    public Main() {

        Settings.loadSettings();

        try {
            DriverManager.registerDriver(new Driver());
            sqlConnection = DriverManager.getConnection(URL, Settings.USER, Settings.PASSWORD);
        } catch (final SQLException ex) {
            throw new RuntimeException("Error connecting to the database", ex);
        }

        final API httpApi = new API(Integer.parseInt(Settings.API_PORT));
        httpApi.start();

        initJDA();

        // MySQL.loadPollTimer();

        topGGIntegration = new TopGGIntegration(getJda());

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                topGGIntegration.postServerCount();
            }
        }, 0, 30*60*1000); //every 30 minutes

        // DAO Handler
        daoHandler = new DaoHandler();

        commandManager = new CommandManager();
        initCommandHandlers(commandManager);

        musicBotManager = new MusicBotManager();
        musicManager = new MusicManager();

    }

    /**
     * Initial the de.progen_bot.command handlers.
     *
     * @param commandManager the de.progen_bot.command manager
     */
    private static void initCommandHandlers(final CommandManager commandManager) {
        commandManager.setupCommandHandlers(new CommandClear());
        commandManager.setupCommandHandlers(new CommandGuildInfo());
        commandManager.setupCommandHandlers(new CommandStatus());
        commandManager.setupCommandHandlers(new CommandSay());
        commandManager.setupCommandHandlers(new CommandUserInfo());
        commandManager.setupCommandHandlers(new CommandWarn());
        commandManager.setupCommandHandlers(new CommandMute());
        commandManager.setupCommandHandlers(new CommandPrivateVoiceChannel());
        commandManager.setupCommandHandlers(new CommandHelp());
        commandManager.setupCommandHandlers(new GameConnectFour());
        commandManager.setupCommandHandlers(new CommandXPrank());
        commandManager.setupCommandHandlers(new CommandXP());
        commandManager.setupCommandHandlers(new CommandXPNotify());
        commandManager.setupCommandHandlers(new CommandMusic());
        commandManager.setupCommandHandlers(new CommandRegisterAPI());
        commandManager.setupCommandHandlers(new CommandWarnList());
        commandManager.setupCommandHandlers(new CommandTempChannel());
        commandManager.setupCommandHandlers(new ChangePrefix());
        commandManager.setupCommandHandlers(new CommandWarnDelete());
        commandManager.setupCommandHandlers(new CommandVote());
        commandManager.setupCommandHandlers(new CommandStop());
        commandManager.setupCommandHandlers(new CommandKick());
        commandManager.setupCommandHandlers(new CommandInfo());
        commandManager.setupCommandHandlers(new CommandBan());
        commandManager.setupCommandHandlers(new CommandPlaylist());
        commandManager.setupCommandHandlers(new CommandUserVoted());
        commandManager.setupCommandHandlers(new CommandNotify());
        commandManager.setupCommandHandlers(new CommandTest());
        commandManager.setupCommandHandlers(new CommandAutorole());
        commandManager.setupCommandHandlers(new CommandUpdate());
        commandManager.setupCommandHandlers(new CommandMuteList());
    }

    /**
     * Initial the JDA.
     */
    private static void initJDA() {
        final JDABuilder 
        builder = JDABuilder.createDefault(
            Settings.TOKEN,
            GatewayIntent.GUILD_BANS,            
            GatewayIntent.GUILD_MESSAGES,
            GatewayIntent.GUILD_MEMBERS,
            GatewayIntent.GUILD_VOICE_STATES,
            GatewayIntent.GUILD_PRESENCES,
            GatewayIntent.GUILD_MESSAGE_REACTIONS,
            GatewayIntent.DIRECT_MESSAGE_REACTIONS,
            GatewayIntent.DIRECT_MESSAGES
        )
            .enableCache(
                    CacheFlag.ACTIVITY
                    )
                    .setMemberCachePolicy(MemberCachePolicy.ALL);
                 

        BuildManager.addEventListeners(builder);
        try {
            jda = builder.build().awaitReady();
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the jda.
     *
     * @return the jda
     */
    public static JDA getJda() {
        return jda;
    }

    public static Connection getSqlConnection() {
        return sqlConnection;
    }

    public static CommandManager getCommandManager() {
        return commandManager;
    }

    public static DaoHandler getDAOs() {
        return daoHandler;
    }

    public static TopGGIntegration getTopGG() {
        return topGGIntegration;
    }

    // Music
    public static MusicBotManager getMusicBotManager() {
        return musicBotManager;
    }

    public static MusicManager getMusicManager() {
        return musicManager;
    }

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(final String[] args) {
        new Main();
    }
}
