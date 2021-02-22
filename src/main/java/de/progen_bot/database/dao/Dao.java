package de.progen_bot.database.dao;

import de.progen_bot.database.connection.ConnectionFactory;

import java.sql.Connection;

public abstract class Dao
{
    public final Connection connection = ConnectionFactory.getConnection();

    public abstract String generateTables();
}
