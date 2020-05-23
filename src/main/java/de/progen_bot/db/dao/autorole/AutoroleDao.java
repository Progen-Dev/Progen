package de.progen_bot.db.dao.autorole;

import net.dv8tion.jda.api.entities.Guild;

import javax.management.relation.Role;
import java.util.List;

public interface AutoroleDao {

    void insertAutorole(Role role, Guild guild);
    List<String> loadAutorole(Role role, Guild guild);
    void deleteAutorole(Role role, Guild guild);
}
