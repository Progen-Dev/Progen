package de.progen_bot.database.dao;

import de.progen_bot.database.connection.ConnectionFactory;
import de.progen_bot.utils.logger.Logger;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DaoHandler
{
    public DaoHandler(Reflections reflections)
    {
        final List<Dao> daoList = new ArrayList<>();
        final Set<Class<? extends Dao>> daos = reflections.getSubTypesOf(Dao.class);
        daos.forEach(c ->
        {
            try
            {
                final Dao o = (Dao) c.getDeclaredConstructors()[0].newInstance();
                daoList.add(o);
            }
            catch (InstantiationException | IllegalAccessException | InvocationTargetException e)
            {
                e.printStackTrace();
            }
        });

        daoList.forEach(dao ->
        {
            try
            {
                if (dao.generateTables().trim().isEmpty())
                    return;

                Logger.debug("Generated table for DAO " + dao.getClass().getSimpleName());

                ConnectionFactory.getConnection().createStatement().executeUpdate(dao.generateTables());
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        });
    }
}
