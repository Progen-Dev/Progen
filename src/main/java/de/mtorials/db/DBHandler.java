package de.mtorials.db;

import de.progen_bot.db.MySQL;

import java.util.ArrayList;

public class DBHandler {

    private ArrayList<DBWrapping> wrappings;
    private MySQL mySQL;

    public DBHandler(MySQL mySQL, ArrayList<DBWrapping> wrappings) {

        this.mySQL = mySQL;
        this.wrappings = wrappings;
    }
}
