package de.mtorials.db.dao;

import de.mtorials.models.Warn;
import de.progen_bot.core.Main;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class DAOWarnList extends DAO {

    @Override
    public void generateTables() {

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
        ResultSet rs = super.getMySQLConnection().query("SELECT 'reason' FROM 'warn' WHERE 'userid' = '" + member.getUser().getId() + "' AND 'guildid' = '" + member.getGuild().getId() + "'");
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
        ResultSet rs = super.getMySQLConnection().query("SELECT 'userid' FROM 'warn' WHERE 'guildid' = '" + guild.getId() + "'");
        try {
            while (rs.next()) {

                warnedUserIDs.add(rs.getString("userid"));
            }
        } catch (SQLException e ) {
            e.printStackTrace();
        }

        HashMap<Member, ArrayList<Warn>> warnsByMembers = new HashMap<>();

        for (String id : warnedUserIDs) {

            Member currentMember = guild.getMemberById(id);
            warnsByMembers.put(currentMember, getWarnsByMember(currentMember));
        }
        return warnsByMembers;
    }

    public void deleteWarnsForMember(Member member) {

        super.getMySQLConnection().update("DELETE FROM 'warns' WHERE 'guildid' = '" + member.getGuild().getId() + "' AND 'userid' = '" + member.getUser().getId() + "'");
    }
}
