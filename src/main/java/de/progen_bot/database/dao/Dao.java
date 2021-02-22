package de.progen_bot.database.dao;

import de.progen_bot.database.connection.ConnectionFactory;

import java.sql.Connection;

public abstract class Dao
{
    protected final Connection connection = ConnectionFactory.getConnection();

    public abstract String generateTables();
}
