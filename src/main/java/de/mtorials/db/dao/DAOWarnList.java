package de.mtorials.db.dao;

import de.mtorials.models.Warn;
import de.progen_bot.core.Main;
import net.dv8tion.jda.api.entities.Guild;
import java.lang.reflect.Member;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class DAOWarnList extends DAO {

    @Override
    public void generateTables() {

        super.getMySQLConnection().update(" CREATE TABLE IF NOT EXISTS warn ( id INT NOT NULL AUTO_INCREMENT , guildid VARCHAR(18) NOT NULL , userid VARCHAR(18) NOT NULL , reason VARCHAR(100) NOT NULL , UNIQUE (id)) ENGINE = InnoDB;");
    }

    public HashMap<String, Warn> getWarnsByID() {

        HashMap<String, Warn> warnsByUser = new HashMap<>();
        ResultSet rs = super.getMySQLConnection().query("SELECT * FROM 'warn'");
        try {
            while (rs.next()) {

                warnsByUser.put(rs.getString("id"), new Warn(Main.getJda().getGuildById(rs.getString("guildid")).getMemberById(rs.getString("userid")), rs.getString("reason")));
            }
        } catch (SQLException e ) {
            e.printStackTrace();
        }
        return warnsByUser;
    }

    public ArrayList<Warn> getWarnsByMember(Member member) {

        ArrayList<Warn> warns = new ArrayList<>();
        ResultSet rs = super.getMySQLConnection().query("SELECT reason FROM `warn` WHERE userid = '" + member.getUser.getId() + "' AND guildid = '" + member.getGuild().getId() + "'");
        try {
            while (rs.next()) {

                warns.add(new Warn(member, rs.getString("reason")));
            }
        } catch (SQLException e ) {
            e.printStackTrace();
        }
        return warns;
    }

    public HashMap<Member, ArrayList<Warn>> getWarnsByMembersForGuild(Guild guild) {

        ArrayList<String> warnedUserIDs = new ArrayList<>();
        ResultSet rs = super.getMySQLConnection().query("SELECT userid FROM warn WHERE guildid = '" + guild.getId() + "'");
        try {
            while (rs.next()) {

                warnedUserIDs.add(rs.getString("userid"));
            }
        } catch (SQLException e ) {
            e.printStackTrace();
        }

        HashMap<Member, ArrayList<Warn>> warnsByMembers = new HashMap<>();

        for (String id : warnedUserIDs) {

            Member currentMember = (Member) guild.getMemberById(id);
            warnsByMembers.put(currentMember, getWarnsByMember(currentMember));
        }
        return warnsByMembers;
    }

    // CRUD

    public void deleteWarnForMember(Member member) {

        super.getMySQLConnection().update("DELETE FROM warn WHERE guildid = '" + member.getUser().getId() + "' AND userid = '" + member.getUser().getId() + "'");
    }

    public void addWarnForMember(Member member, String reason) {

        super.getMySQLConnection().update("INSERT INTO warn (userid, guildid, reason) VALUES ('" + member.getUser().getId() + "', '" + member.getGuild().getId() + "', '" + reason + "')");
    }
}
