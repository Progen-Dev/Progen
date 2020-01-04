package de.progen_bot.db.dao.autorole;

import de.progen_bot.db.connection.ConnectionFactory;
import de.progen_bot.db.dao.Dao;
import net.dv8tion.jda.api.entities.Guild;

import javax.management.relation.Role;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AutoroleDaoImpl extends Dao implements AutoroleDao {
    final private String sqlQuery = "CREATE TABLE IF NOT EXISTS autorole(`id` INT(11) NOT NULL AUTO_INCREMENT, ´guildid´" + "VARCHAR(50) NOT NULL, `roleid`VARCHAR(50) NOT NULL" +
            "PRIMARY KEY(`id`)) ENGINE = InnoDB DEFAULT CHARSET = utf8";

    @Override
    public void insertautorole(Role role, Guild guild) {
        Connection connection = ConnectionFactory.getConnection();
        try{
            PreparedStatement ps = connection.prepareStatement("INSERT INTO `autorole` (guildid, role) VALUES (?,?,?)");
            ps.setString(1, role.getRoleName());
            ps.setString(2, guild.getId());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteautorole(Role role, Guild guild) {
        Connection connection = ConnectionFactory.getConnection()

    }
}
