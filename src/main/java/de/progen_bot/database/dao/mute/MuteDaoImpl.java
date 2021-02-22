package de.progen_bot.database.dao.mute;

import de.progen_bot.database.dao.Dao;
import de.progen_bot.database.entities.mute.MuteData;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MuteDaoImpl extends Dao implements MuteDao
{
    private static MuteData setMuteDataFromResultSet(ResultSet set) throws SQLException
    {
        return new MuteData(set.getString(1), set.getString(2), set.getString(3), set.getString(4));
    }

    @Override
    public void deleteMute(MuteData data)
    {
        if (data == null)
            return;

        try
        {
            final PreparedStatement ps = connection.prepareStatement(
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

    @Override
    public void saveMute(MuteData data)
    {
        try
        {
            final PreparedStatement ps = connection.prepareStatement(
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

    @Override
    public MuteData getMute(String victimId)
    {
        try
        {
            final PreparedStatement ps = connection.prepareStatement(
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

    @Override
    public List<MuteData> getMutesByGuild(String guildId)
    {
        List<MuteData> mutes = new ArrayList<>();

        try
        {
            final PreparedStatement ps = connection.prepareStatement("SELECT * FROM `mute` WHERE `guild` = ?");
            ps.setString(1, guildId);

            final ResultSet rs = ps.executeQuery();
            while (rs.next())
            {
                mutes.add(setMuteDataFromResultSet(rs));
            }
            return mutes;
        }
        catch (SQLException e)
        {
            return Collections.emptyList();
        }
    }

    @Override
    public String generateTables()
    {
        return "CREATE TABLE IF NOT EXISTS mute(`victim` VARCHAR(50) NOT NULL, `reason` " +
                "VARCHAR(255) NOT NULL, `executor` VARCHAR(50) NOT NULL, `guild` VARCHAR(50) NOT NULL)"
                + "ENGINE = InnoDB DEFAULT CHARSET = utf8;";
    }
}
