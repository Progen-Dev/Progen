package de.progen_bot.db.dao.autorole;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import javax.management.relation.Role;
import java.util.List;

public interface AutoroleDao {

    void insertautorole(Role role, Guild guild);
    List<String>loadautorole(Role role, Guild guild);
        void deleteautorole(Role role, Guild guild);
}
