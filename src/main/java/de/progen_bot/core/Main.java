package de.progen_bot.core;

import com.mysql.cj.jdbc.Driver;
import de.mtorials.commands.ChangePrefix;
import de.mtorials.fortnite.core.Fortnite;
import de.mtorials.pwi.httpapi.API;
import de.progen_bot.command.CommandManager;
import de.progen_bot.commands.Settings.CommandNotify;
import de.progen_bot.commands.Settings.CommandVote;
import de.progen_bot.commands.fun.ConnectFour;
import de.progen_bot.commands.Help;
import de.progen_bot.commands.moderator.*;
import de.progen_bot.commands.moderator.Blacklist.CommandBan;
import de.progen_bot.commands.moderator.Blacklist.CommandKick;
import de.progen_bot.commands.music.CommandPlaylist;
import de.progen_bot.commands.user.*;
import de.progen_bot.commands.owner.CommandRestart;
import de.progen_bot.commands.owner.CommandStop;
import de.progen_bot.commands.music.CommandMusic;
import de.progen_bot.commands.xp.XP;
import de.progen_bot.commands.xp.XPNotify;
import de.progen_bot.commands.xp.XPrank;
import de.progen_bot.db.DaoHandler;
import de.progen_bot.music.MusicManager;
import de.progen_bot.util.Settings;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * The Class Main.
 */
public class Main {

    /**
     * The jda.
     */

    private static String URL;

    private static JDA jda;

    private static Connection sqlConnection;

    private static Fortnite fortnite;

    private static API httpapi;

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

        URL = "jdbc:mysql://" + Settings.HOST + ":" + Settings.PORT + "/" +
                Settings.DATABASE + "?useUnicode=true&serverTimezone=UTC&autoReconnect=true";

        try {
            DriverManager.registerDriver(new Driver());
            sqlConnection = DriverManager.getConnection(URL, Settings.USER, Settings.PASSWORD);
        } catch (SQLException ex) {
            throw new RuntimeException("Error connecting to the database", ex);
        }

        httpapi = new API(Integer.parseInt(Settings.APIPORT));
        httpapi.start();

        fortnite = new Fortnite();

        initJDA();

        //TODO MySQL.loadPollTimer();

        topGGIntegration = new TopGGIntegration(getJda(), Settings.TOPGGTOKEN);
        topGGIntegration.postServerCount();

        // DAO Handler
        daoHandler = new DaoHandler();

        commandManager = new CommandManager();
        initCommandHandlers(commandManager);

        musicBotManager = new MusicBotManager();
        musicManager = new MusicManager();


    }

    /**
     * Inits the de.progen_bot.command handlers.
     *
     * @param commandManager the de.progen_bot.command manager
     */
    private void initCommandHandlers(CommandManager commandManager) {
        commandManager.setupCommandHandlers(new Clear());
        commandManager.setupCommandHandlers(new GuildInfo());
        commandManager.setupCommandHandlers(new CommandPing());
        commandManager.setupCommandHandlers(new Say());
        commandManager.setupCommandHandlers(new CommandUserInfo());
        commandManager.setupCommandHandlers(new Warn());
        commandManager.setupCommandHandlers(new CommandMute());
        commandManager.setupCommandHandlers(new UnMute());
        commandManager.setupCommandHandlers(new PrivateVoiceChannel());
        commandManager.setupCommandHandlers(new Help());
        commandManager.setupCommandHandlers(new ConnectFour());
        commandManager.setupCommandHandlers(new XPrank());
        commandManager.setupCommandHandlers(new XP());
        commandManager.setupCommandHandlers(new XPNotify());
        commandManager.setupCommandHandlers(new CommandMusic());
        commandManager.setupCommandHandlers(new CommandRegisterAPI());
        commandManager.setupCommandHandlers(new WarnList());
        commandManager.setupCommandHandlers(new CmdTempChannel());
        commandManager.setupCommandHandlers(new ChangePrefix());
        commandManager.setupCommandHandlers(new WarnDelete());
        commandManager.setupCommandHandlers(new CommandVote());
        commandManager.setupCommandHandlers(new CommandStop());
        commandManager.setupCommandHandlers(new CommandRestart());
        commandManager.setupCommandHandlers(new CommandKick());
        commandManager.setupCommandHandlers(new CommandInfo());
        commandManager.setupCommandHandlers(new CommandBan());
        commandManager.setupCommandHandlers(new CommandPlaylist());
        commandManager.setupCommandHandlers(new UserVotet());
        commandManager.setupCommandHandlers(new CommandNotify());
    }

    /**
     * Inits the JDA.
     */
    private void initJDA() {
        JDABuilder builder = new JDABuilder(AccountType.BOT).setToken(Settings.TOKEN);
        builder.setAutoReconnect(true);
        new BuildManager(builder);

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

    public static Fortnite getFortnite() {
        return fortnite;
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
    public static void main(String[] args) throws IOException {
        new Main();
    }
}
