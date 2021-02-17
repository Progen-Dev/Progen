package de.progen_bot.database;

import de.progen_bot.database.connection.ConnectionFactory;
import de.progen_bot.database.dao.Dao;
import de.progen_bot.database.dao.config.ConfigDaoImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DaoHandler
{
    // FIXME: 17.02.2021 extract
    public DaoHandler()
    {
        final List<Dao> daoList = new ArrayList<>(){{
            add(new ConfigDaoImpl());
        }};
        
        daoList.stream().forEach(dao ->
        {
            try
            {
                ConnectionFactory.getConnection().createStatement().executeUpdate(dao.generateTables());
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        });
    }
}
