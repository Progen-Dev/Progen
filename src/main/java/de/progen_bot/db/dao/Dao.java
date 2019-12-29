package de.progen_bot.db.dao;

import de.progen_bot.db.connection.ConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class Dao {
    public void generateTables(String sqlQuery) {
        Connection connection = ConnectionFactory.getConnection();
        try {
            connection.prepareStatement(sqlQuery).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
