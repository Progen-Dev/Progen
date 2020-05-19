package de.progen_bot.db.dao.autorole;

import java.util.List;

import javax.management.relation.Role;

import net.dv8tion.jda.api.entities.Guild;

public interface AutoroleDao {

    void insertautorole(Role role, Guild guild);
    List<String> loadautorole(Role role, Guild guild);
    void deleteautorole(Role role, Guild guild);
}
