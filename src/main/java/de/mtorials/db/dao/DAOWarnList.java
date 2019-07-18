package de.mtorials.db.dao;

import de.mtorials.models.Warn;
import de.progen_bot.core.Main;
import net.dv8tion.jda.core.entities.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class DAOWarnList extends DAO {

    @Override
    public void generateTable() {

    }

    private HashMap<String, Warn> getWarnsByID() {

        HashMap<String, Warn> warnsByUser = new HashMap<>();
        ResultSet rs = super.getMySQLConnection().query("SELECT * FROM 'warn'");
        try {
            while (rs.next()) {

                warnsByUser.put(rs.getString("id"), new Warn(Main.getJda().getUserById(rs.getString("userid")), rs.getString("reason")));
            }
        } catch (SQLException e ) {
            e.printStackTrace();
        }
        return warnsByUser;
    }

    // Public


}
