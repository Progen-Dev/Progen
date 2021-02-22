package de.progen_bot.database.dao.connectfour;

import de.progen_bot.database.dao.Dao;
import de.progen_bot.database.entities.connectfour.ConnectFourModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectFourDaoImpl extends Dao implements ConnectFourDao
{
    @Override
    public ConnectFourModel getConnectFourData(String msgId)
    {
        final PreparedStatement statement;
        try
        {
            statement = connection.prepareStatement("SELECT * FROM `fourgame` WHERE `msgid` = ?");
            statement.setString(1, msgId);
            final ResultSet rs = statement.executeQuery();
            if (rs.next())
            {
                String[] rows = rs.getString(5).split("\n");
                char[][] loadedBoard = new char[rows.length][rows.length + 1];
                for (int i = 0; i < rows.length; i++)
                {
                    String[] colums = rows[i].split(";");
                    for (int j = 0; j < colums.length; j++)
                    {
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
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void insertConnectFourData(ConnectFourModel data)
    {
        try
        {
            final PreparedStatement ps = connection.prepareStatement(
                    "REPLACE INTO fourgame (msgid,height,width,actplayer,board,player1,player2,gameover,counter) " +
                            "VALUES (?,?,?,?,?,?,?,?,?)");
            final char[][] board = data.getBoard();
            // set input parameters
            final StringBuilder boardInText = new StringBuilder(); // height , width
            for (char[] chars : board)
            {
                for (int i = 0; i < board[0].length; i++)
                {
                    boardInText.append(chars[i]).append(";");
                }
                boardInText.append("\n");
            }
            ps.setString(1, data.getMsgId());
            ps.setInt(2, board.length);
            ps.setInt(3, board[0].length);
            ps.setString(4, data.getActPlayer());
            ps.setString(5, boardInText.toString());
            ps.setString(6, data.getPlayer1());
            ps.setString(7, data.getPlayer2());
            ps.setBoolean(8, data.isGameOver());
            ps.setInt(9, data.getCounter());
            ps.execute();
            ps.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public String generateTables()
    {
        return "CREATE TABLE IF NOT EXISTS fourgame( `msgid` varchar(50) NOT NULL," +
                "`height` INT(1) NOT NULL, `width` INT(1) NOT NULL, `actplayer`VARCHAR(50) NOT NULL, `board` TINYTEXT NOT " +
                "NULL,`player1`VARCHAR(50) NOT NULL,`player2`VARCHAR(50) NOT NULL, `gameover`TINYINT(1) NOT NULL, " +
                "`counter` INT(2) NOT NULL, PRIMARY KEY(`msgid`) ) ENGINE = InnoDB DEFAULT CHARSET = utf8";
    }
}
