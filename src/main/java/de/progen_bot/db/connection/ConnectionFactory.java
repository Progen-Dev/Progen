package de.progen_bot.db.connection;

import de.progen_bot.core.Main;

import java.sql.Connection;

public class ConnectionFactory {

    private ConnectionFactory() {
        /* Prevent instantiation */
    }

    /**
     * Get a connection to database
     *
     * @return Connection object
     */
    public static Connection getConnection() {
        return Main.getSqlConnection();
    }
}
