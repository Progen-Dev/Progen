package de.progen_bot.db.dao.XP;

import java.lang.reflect.Member;
import java.sql.ResultSet;


import de.mtorials.db.dao.DAO;
import de.progen_bot.commands.xp.XP;

public class DAOXP extends DAO implements DAOXPInterface {

    @Override
    public XP getXPForMember(Member member) {
        ResultSet rs = super.getMySQLConnection().query("SELECT * FROM 'xp' WHERE 'id' WHERE 'userid' ");

            try {

            }
    }

    @Override
    public void generateTables() {
        super.getMySQLConnection().update("CREATE TABLE IF NOT EXIST xp (id INT NOT NULL AUTO_INCREMENT, guildid VARCHAR(50) NOT NULL, userid VARCHAR(50) NOT NULL, totalxp BIGNIT(12) NOT NULL, level INT NOT NULL, notify TINYINT(1)) ENGINE = INNODB");
    }
}
