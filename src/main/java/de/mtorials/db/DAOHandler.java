package de.mtorials.db;

import de.mtorials.db.dao.DAO;
import de.mtorials.db.dao.DAOWarnList;

import java.util.ArrayList;

public class DAOHandler {

    //All DAOs
    private ArrayList<DAO> daos = new ArrayList<>();

    //DOA Classes
    private DAOWarnList warnList;

    public DAOHandler() {

        // Add all DOAs to Handler
        warnList = new DAOWarnList();
        daos.add(warnList);

        // Execute DOA Methods
        for (DAO dao : daos) {

            dao.generateTables();
        }
    }

    // Getter

    public DAOWarnList getWarnList() {
        return warnList;
    }
}
