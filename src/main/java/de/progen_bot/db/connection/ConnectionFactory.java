package de.progen_bot.db.connection;

import com.mysql.cj.jdbc.Driver;
import de.progen_bot.core.Main;
import de.progen_bot.util.Settings;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    /**
     * Get a connection to database
     *
     * @return Connection object
     */
    public static Connection getConnection() {
        return Main.getSqlConnection();
    }
}
