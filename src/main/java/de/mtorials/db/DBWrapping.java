package de.mtorials.db;

import de.progen_bot.db.MySQL;

public abstract class DBWrapping {

    private MySQL mySQL;
    public abstract void onStart();

    public DBWrapping(MySQL mySQL) {

        this.mySQL = mySQL;
    }

    public MySQL getMySQL() {

        return mySQL;
    }
}
