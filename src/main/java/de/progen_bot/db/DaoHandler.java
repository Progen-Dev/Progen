package de.progen_bot.db;

import de.progen_bot.db.dao.Dao;
import de.progen_bot.db.dao.autorole.AutoroleDaoImpl;
import de.progen_bot.db.dao.config.ConfigDaoImpl;
import de.progen_bot.db.dao.connectfour.ConnectFourDaoImpl;
import de.progen_bot.db.dao.playlist.PlaylistDaoImpl;
import de.progen_bot.db.dao.poll.PollDaoImpl;
import de.progen_bot.db.dao.tokenmanager.TokenManagerDao;
import de.progen_bot.db.dao.warnlist.WarnListDaoImpl;
import de.progen_bot.db.dao.xp.XpDaoImpl;

import java.util.ArrayList;

public class DaoHandler {

	public DaoHandler() {
		ArrayList<Dao> daoList = new ArrayList<>();
		daoList.add(new XpDaoImpl());
        daoList.add(new WarnListDaoImpl());
        daoList.add(new PollDaoImpl());
        daoList.add(new ConnectFourDaoImpl());
        daoList.add(new TokenManagerDao());
        daoList.add(new ConfigDaoImpl());
        daoList.add(new PlaylistDaoImpl());
        daoList.add(new AutoroleDaoImpl());

        for (Dao dao : daoList) {
            dao.generateTables("");
        }
    }
}
