package de.progen_bot.db.dao.mute;

import de.progen_bot.db.connection.ConnectionFactory;
import de.progen_bot.db.dao.Dao;
import de.progen_bot.db.entities.MuteData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MuteDao extends Dao
{
    public void deleteMute(MuteData data)
    {
        if (data == null)
            return;

        final Connection con = ConnectionFactory.getConnection();
        try
        {
            final PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM `mute` WHERE `victim` = ? AND `guild` = ?;"
            );
            ps.setString(1, data.getVictimId());
            ps.setString(2, data.getGuildId());
            ps.execute();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void saveMute(MuteData data)
    {
        final Connection con = ConnectionFactory.getConnection();

        try
        {
            final PreparedStatement ps = con.prepareStatement(
                    "REPLACE INTO `mute` (victim, reason, executor, guild) VALUES (?, ?, ?, ?);"
            );
            ps.setString(1, data.getVictimId());
            ps.setString(2, data.getReason());
            ps.setString(3, data.getExecutorId());
            ps.setString(4, data.getGuildId());
            ps.execute();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public MuteData getMute(String victimId)
    {
        final Connection con = ConnectionFactory.getConnection();

        try
        {
            final PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM `mute` WHERE `victim` = ?;"
            );
            ps.setString(1, victimId);

            final ResultSet set = ps.executeQuery();
            if (set.next())
                return setMuteDataFromResultSet(set);
            else
                return null;
        }
        catch (SQLException e)
        {
            return null;
        }
    }

    private static MuteData setMuteDataFromResultSet(ResultSet set) throws SQLException
    {
        return new MuteData(set.getString(1), set.getString(2), set.getString(3), set.getString(4));
    }

    @Override
    public void generateTables(String sqlQuery)
    {
        final String query = "CREATE TABLE IF NOT EXISTS mute(`victim` VARCHAR(50) NOT NULL, `reason` " +
                "VARCHAR(255) NOT NULL, `executor` VARCHAR(50) NOT NULL, `guild` VARCHAR(50) NOT NULL)"
                + "ENGINE = InnoDB DEFAULT CHARSET = utf8";
        super.generateTables(query);
    }
}
