package de.progen_bot.core;

import de.mtorials.commands.ChangePrefix;
import de.mtorials.commands.Stats;
import de.mtorials.db.DAOHandler;
import de.mtorials.db.MySQLConnection;
import de.mtorials.fortnite.core.Fortnite;
import de.mtorials.pwi.httpapi.API;
import de.progen_bot.command.CommandManager;
import de.progen_bot.commands.Administartor.CommandRestart;
import de.progen_bot.commands.Administartor.CommandStop;
import de.progen_bot.commands.Fun.ConnectFour;
import de.progen_bot.commands.Help;
import de.progen_bot.commands.Moderator.*;
import de.progen_bot.commands.User.*;
import de.progen_bot.commands.music.Music;
import de.progen_bot.commands.xp.XP;
import de.progen_bot.commands.xp.XPNotify;
import de.progen_bot.commands.xp.XPrank;
import de.progen_bot.db.MySQL;
import de.progen_bot.util.Settings;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;
import java.io.IOException;

/**
 * The Class Main.
 */
public class Main {

    /**
     * The jda.
     */
    private static JDA jda;

    private static MySQL sql;

    private static MySQLConnection mySQLConnection;

    private static Fortnite fortnite;

    private static API httpapi;

    private static CommandManager commandManager;

    //DAOs

    private static DAOHandler daoHandler;

    /**
     * Instantiates a new main.
     */
    public Main() throws IOException {

        Settings.loadSettings();

        mySQLConnection  = new MySQLConnection(Settings.HOST, Settings.DATABASE, Settings.DATABASE, Settings.PASSWORD);

        httpapi = new API(8083);
        httpapi.start();

        fortnite = new Fortnite();

        MySQL.connect();

        initJDA();

        MySQL.loadPollTimer();

        // DAO Handler
        daoHandler = new DAOHandler();

        commandManager = new CommandManager();
        initCommandHandlers(commandManager);
    }

    /**
     * Inits the de.progen_bot.command handlers.
     *
     * @param commandManager the de.progen_bot.command manager
     */
    private void initCommandHandlers(CommandManager commandManager) {
        commandManager.setupCommandHandlers(new Clear());
        commandManager.setupCommandHandlers(new GuildInfo());
        commandManager.setupCommandHandlers(new Ping());
        commandManager.setupCommandHandlers(new Say());
        commandManager.setupCommandHandlers(new CommandUserInfo());
        commandManager.setupCommandHandlers(new Warn());
        commandManager.setupCommandHandlers(new Mute());
        commandManager.setupCommandHandlers(new UnMute());
        commandManager.setupCommandHandlers(new PrivateVoiceChannel());
        //commandManager.setupCommandHandlers(new VierGewinnt());
        commandManager.setupCommandHandlers(new Help());
        commandManager.setupCommandHandlers(new ConnectFour());
        commandManager.setupCommandHandlers(new XPrank());
        commandManager.setupCommandHandlers(new XP());
        commandManager.setupCommandHandlers(new XPNotify());
        commandManager.setupCommandHandlers(new Music());
        commandManager.setupCommandHandlers(new Stats());
        commandManager.setupCommandHandlers(new CommandRegisterAPI());
        commandManager.setupCommandHandlers(new WarnList());
        commandManager.setupCommandHandlers(new CmdTempChannel());
        commandManager.setupCommandHandlers(new ChangePrefix());
        commandManager.setupCommandHandlers(new WarnDelete());
        commandManager.setupCommandHandlers(new Vote());
        commandManager.setupCommandHandlers(new CommandStop());
        commandManager.setupCommandHandlers(new CommandRestart());
        commandManager.setupCommandHandlers(new kick());
        commandManager.setupCommandHandlers(new CommandInfo());
    }

    /**
     * Inits the JDA.
     */
    private void initJDA() {
        JDABuilder builder = new JDABuilder(AccountType.BOT).setToken(Settings.TOKEN);

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

    public static MySQL getSQL() {
        return sql;
    }

    public static Fortnite getFortnite() {
        return fortnite;
    }

    public static CommandManager getCommandManager() {
        return commandManager;
    }

    public static MySQLConnection getMysqlConnection() {

        return mySQLConnection;
    }

    public static DAOHandler getDAOs() {

        return daoHandler;
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
