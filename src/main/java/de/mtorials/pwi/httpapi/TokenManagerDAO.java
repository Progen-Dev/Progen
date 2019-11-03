package de.mtorials.pwi.httpapi;

import de.mtorials.db.dao.DAO;
import de.progen_bot.core.Main;
import net.dv8tion.jda.api.entities.Member;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TokenManagerDAO extends DAO {

    @Override
    public void generateTables() {

        super.getMySQLConnection().update("CREATE TABLE `tokens` ( `guildid` VARCHAR(18) NOT NULL , `userid` VARCHAR(18) NOT NULL , `token` VARCHAR(10) NOT NULL , `time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP , PRIMARY KEY (`token`)) ENGINE = InnoDB;");
    }

    boolean keyExists(String token) throws SQLException {

        ResultSet rs = super.getMySQLConnection().query("SELECT token FROM tokens WHERE token = '" + token + "'");
        return rs.next();
    }

    boolean memberExists(Member member) throws SQLException {

        ResultSet rs = super.getMySQLConnection().query("SELECT token FROM tokens WHERE userid = '" + member.getUser().getId() + "' AND guildid = '" + member.getGuild().getId() + "'");
        return rs.next();
    }

    void addMember(String token, Member member) {

        super.getMySQLConnection().update("INSERT INTO tokens (guildid, userid, token) VALUES ('" + member.getGuild().getId() + "', '" + member.getUser().getId() + "', '" + token + "')");
    }

    Member getMember(String token) throws SQLException {

        ResultSet rs = super.getMySQLConnection().query("SELECT userid, guildid FROM tokens WHERE token = '" + token + "'");
        if (!rs.next()) return null;
        return Main.getJda().getGuildById(rs.getString("guildid")).getMemberById(rs.getString("userid"));
    }

    void deleteMember(Member member) {

        super.getMySQLConnection().update("DELETE FROM tokens WHERE userid = '" + member.getUser().getId() + "' AND guildid = '" + member.getGuild().getId() + "'");
    }
}
