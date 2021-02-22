package de.progen_bot.database;

import de.progen_bot.database.connection.ConnectionFactory;
import de.progen_bot.database.dao.Dao;
import de.progen_bot.database.dao.config.ConfigDaoImpl;
import de.progen_bot.database.dao.connectfour.ConnectFourDaoImpl;
import de.progen_bot.database.dao.connectfour.GameDataDaoImpl;
import de.progen_bot.database.dao.mute.MuteDaoImpl;
import de.progen_bot.database.dao.poll.PollDaoImpl;
import de.progen_bot.database.dao.tokenmanager.TokenManagerDaoImpl;
import de.progen_bot.database.dao.warnlist.WarnListDaoImpl;
import de.progen_bot.database.dao.xp.XpDaoImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DaoHandler
{
    // TODO: 22.02.2021 adding using reflections?
    public DaoHandler()
    {
        final List<Dao> daoList = new ArrayList<>();
        daoList.add(new ConfigDaoImpl());
        daoList.add(new WarnListDaoImpl());
        daoList.add(new TokenManagerDaoImpl());
        daoList.add(new ConnectFourDaoImpl());
        daoList.add(new GameDataDaoImpl());
        daoList.add(new XpDaoImpl());
        daoList.add(new MuteDaoImpl());
        daoList.add(new PollDaoImpl());
        
        daoList.forEach(dao ->
        {
            try
            {
                if (dao.generateTables().trim().isEmpty())
                    return;

                ConnectionFactory.getConnection().createStatement().executeUpdate(dao.generateTables());
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        });
    }
}
