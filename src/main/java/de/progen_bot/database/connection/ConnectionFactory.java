package de.progen_bot.database.connection;

import de.progen_bot.core.Main;

import java.sql.Connection;

// FIXME: 17.02.2021 use Main.getConnection() instead (JVM performance)
public class ConnectionFactory
{
    private ConnectionFactory()
    {
        /* Prevent instantiation */
    }

    public static Connection getConnection()
    {
        return Main.getConnection();
    }
}
