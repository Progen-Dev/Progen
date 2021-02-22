package de.progen_bot.database.dao.tokenmanager;

import de.progen_bot.core.Main;
import de.progen_bot.database.dao.Dao;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TokenManagerDaoImpl extends Dao implements TokenManagerDao
{
    @Override
    public String generateTables()
    {
        return "CREATE TABLE if not exists `tokens`(`guildid` VARCHAR(18) NOT NULL, `userid` " +
				"VARCHAR(18) NOT NULL , `token` VARCHAR(10) NOT NULL , `time` TIMESTAMP NOT NULL DEFAULT " +
				"CURRENT_TIMESTAMP, PRIMARY KEY (`token`)) ENGINE = InnoDB;";
    }

    @Override
    public boolean keyExists(String token) throws SQLException
    {
        final PreparedStatement ps = this.connection.prepareStatement("SELECT `token` FROM `tokens` WHERE `token` = ?;");

        ps.setString(1, token);

        final ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    @Override
    public boolean memberExists(Member member) throws SQLException
    {
        final PreparedStatement ps = this.connection.prepareStatement("SELECT `token` FROM `tokens` WHERE `userid` = ? AND `guildid` = ?;");

        ps.setString(1, member.getId());
        ps.setString(2, member.getGuild().getId());

        final ResultSet rs = ps.executeQuery();

        return rs.next();
    }

    @Override
    public void addMember(String token, Member member) throws SQLException
    {
        final PreparedStatement ps = this.connection.prepareStatement("INSERT INTO tokens (guildid, userid, token) VALUES (?, ? ,?);");

        ps.setString(1, member.getGuild().getId());
        ps.setString(2, member.getId());
        ps.setString(3, token);

        ps.execute();
    }

    @Override
    public Member getMember(String token) throws SQLException
    {
        final PreparedStatement ps = this.connection.prepareStatement("SELECT userid, guildid FROM tokens WHERE token = ?;");
        
        ps.setString(1, token);
        
        final ResultSet rs = ps.executeQuery();
        
        if (!rs.next())
            return null;
        
        final Guild guild = Main.getJDA().getGuildById(rs.getString("guildid"));
        if (guild == null)
            return null;

        // FIXME: 17.02.2021 possible to be null
        return guild.getMemberById(rs.getString("userid"));
    }

    @Override
    public void deleteMember(Member member) throws SQLException
    {
        final PreparedStatement ps = this.connection.prepareStatement("DELETE FROM tokens WHERE userid = ? AND guildid = ?");

        ps.setString(1, member.getId());
        ps.setString(2, member.getGuild().getId());

        ps.execute();
    }
}
