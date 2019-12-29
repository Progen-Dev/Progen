package de.progen_bot.db.connection;

import com.mysql.cj.jdbc.Driver;
import de.progen_bot.util.Settings;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private static final String URL = "jdbc:mysql://" + Settings.HOST + ":" + Settings.PORT + "/" +
            Settings.DATABASE + "?useUnicode=true&serverTimezone=UTC&autoReconnect=true";

    /**
     * Get a connection to database
     *
     * @return Connection object
     */
    public static Connection getConnection() {
        try {
            DriverManager.registerDriver(new Driver());
            return DriverManager.getConnection(URL, Settings.USER, Settings.PASSWORD);
        } catch (SQLException ex) {
            throw new RuntimeException("Error connecting to the database", ex);
        }
    }
}
