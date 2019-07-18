package de.mtorials.db.dao;

import de.mtorials.db.MySQLConnection;
import de.progen_bot.core.Main;

public abstract class DAO {

    private MySQLConnection mySQLConnection = Main.getMysqlConnection();

    public abstract void generateTables();

    public MySQLConnection getMySQLConnection() {
        return mySQLConnection;
    }
}
