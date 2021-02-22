package de.progen_bot.database.dao.connectfour;

import de.progen_bot.database.dao.Dao;
import de.progen_bot.database.entities.connectfour.GameData;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GameDataDaoImpl extends Dao implements GameDataDao
{
    @Override
    public GameData getGameData(String messageId) {
        try {
            final PreparedStatement ps = connection.prepareStatement("SELECT * FROM `viergame` WHERE `messageid` = ?");
            ps.setString(1, messageId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                GameData data = new GameData();
                data.setMessageId(rs.getString(1));
                data.setOpponentId(rs.getString(2));
                data.setChallengerId(rs.getString(3));
                data.setHeight(rs.getInt(4));
                data.setWidth(rs.getInt(5));
                data.setChannel(rs.getString(6));
                return data;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void insertGameData(GameData game) {
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO viergame (messageid,opponentid,challengerid,heigh,width,channelid) VALUES(?,?,?,?,?,?)");
            ps.setString(1, game.getMessageId());
            ps.setString(2, game.getOpponentId());
            ps.setString(3, game.getChallengerId());
            ps.setInt(4, game.getHeight());
            ps.setInt(5, game.getWidth());
            ps.setString(6, game.getChannelId());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String generateTables()
    {
        return "CREATE TABLE IF NOT EXISTS viergame ( `messageid` VARCHAR(50) NOT NULL," +
                "`opponentid` VARCHAR(50) NOT NULL, `challengerid` VARCHAR(50) NOT NULL, `heigh` INT(1) NOT NULL," +
                "`width` INT(1) NOT NULL, `channelid`VARCHAR(50) NOT NULL, PRIMARY KEY(`messageid`) ) ENGINE = InnoDB " +
                "DEFAULT CHARSET = utf8";
    }
}
