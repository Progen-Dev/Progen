package de.mtorials.db;

import de.progen_bot.db.MySQL;

import java.util.ArrayList;

public class DBHandlerBuilder {

    private ArrayList<DBWrapping> wrappings = new ArrayList<>();
    private MySQL mySQL;

    public DBHandlerBuilder addCommand(DBWrapping wrapping) {

        this.wrappings.add(wrapping);
        return this;
    }

    public DBHandlerBuilder setMySQL(MySQL mySQL) {

        this.mySQL = mySQL;
        return this;
    }

    public DBHandler build() {

        return new DBHandler(this.mySQL, this.wrappings);
    }
}

