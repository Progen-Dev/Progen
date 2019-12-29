package de.progen_bot.db.dao.connectfour;

import de.progen_bot.db.connection.ConnectionFactory;
import de.progen_bot.db.dao.Dao;
import de.progen_bot.db.entities.ConnectFourModel;
import de.progen_bot.db.entities.GameData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectFourDaoImpl extends Dao implements ConnectFourDao {
    private final String sqlQuery = "CREATE TABLE IF NOT EXISTS viergame ( `messageid` VARCHAR(50) NOT NULL," +
            "`opponentid` VARCHAR(50) NOT NULL, `challengerid` VARCHAR(50) NOT NULL, `heigh` INT(1) NOT NULL," +
            "`width` INT(1) NOT NULL, `channelid`VARCHAR(50) NOT NULL, PRIMARY KEY(`messageid`) ) ENGINE = InnoDB " +
            "DEFAULT CHARSET = utf8";

    private final String sqlQueryGame = "CREATE TABLE IF NOT EXISTS fourgame( `msgid` varchar(50) NOT NULL," +
            "`height` INT(1) NOT NULL, `width` INT(1) NOT NULL, `actplayer`VARCHAR(50) NOT NULL, `board` TINYTEXT NOT " +
            "NULL,`player1`VARCHAR(50) NOT NULL,`player2`VARCHAR(50) NOT NULL, `gameover`TINYINT(1) NOT NULL, " +
            "`counter` INT(2) NOT NULL, PRIMARY KEY(`msgid`) ) ENGINE = InnoDB DEFAULT CHARSET = utf8";

    public GameData getGameData(String messageId) {
        Connection connection = ConnectionFactory.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM `viergame` WHERE `messageid` = ?");
            ps.setString(1, messageId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                GameData data = new GameData();
                data.setMessageId(rs.getString(1));
                data.setOpponentId(rs.getString(2));
                data.setChallengerId(rs.getString(3));
                data.setHeigh(rs.getInt(4));
                data.setWidth(rs.getInt(5));
                data.setChannel(rs.getString(6));
                return data;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insertGameData(GameData game) {
        Connection connection = ConnectionFactory.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO viergame (messageid,opponentid,challengerid,heigh,width,channelid) VALUES(?,?,?,?,?,?)");
            ps.setString(1, game.getMessageId());
            ps.setString(2, game.getOpponentId());
            ps.setString(3, game.getChallengerId());
            ps.setInt(4, game.getHeigh());
            ps.setInt(5, game.getWidth());
            ps.setString(6, game.getChannelId());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertConnectFourData(ConnectFourModel data) {
        Connection connection = ConnectionFactory.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "REPLACE INTO fourgame (msgid,height,width,actplayer,board,player1,player2,gameover,counter) " +
                            "VALUES (?,?,?,?,?,?,?,?,?)");
            char[][] board = data.getBoard();
            // set input parameters
            String boardInText = ""; // height , width
            for (int j = 0; j < board.length; j++) {
                for (int i = 0; i < board[0].length; i++) {
                    boardInText += board[j][i] + ";";
                }
                boardInText += "\n";
            }
            ps.setString(1, data.getMsgId());
            ps.setInt(2, board.length);
            ps.setInt(3, board[0].length);
            ps.setString(4, data.getActPlayer());
            ps.setString(5, boardInText);
            ps.setString(6, data.getPlayer1());
            ps.setString(7, data.getPlayer2());
            ps.setBoolean(8, data.isGameOver());
            ps.setInt(9, data.getCounter());
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ConnectFourModel getConnectFourData(String msgId) {
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement("SELECT * FROM `fourgame` WHERE `msgid` = ?");
            statement.setString(1, msgId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String[] rows = rs.getString(5).split("\n");
                char[][] loadedBoard = new char[rows.length][rows.length + 1];
                for (int i = 0; i < rows.length; i++) {
                    String[] colums = rows[i].split(";");
                    for (int j = 0; j < colums.length; j++) {
                        loadedBoard[i][j] = colums[j].charAt(0);
                    }
                }
                ConnectFourModel cFourModel = new ConnectFourModel(loadedBoard, loadedBoard.length,
                        loadedBoard[0].length, rs.getString(6), rs.getString(7), rs.getString(4), rs.getString(1),
                        rs.getBoolean(8), rs.getInt(9));
                statement.close();
                rs.close();
                return cFourModel;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void generateTables(String query) {
        super.generateTables(sqlQuery);
        super.generateTables(sqlQueryGame);
    }
}
