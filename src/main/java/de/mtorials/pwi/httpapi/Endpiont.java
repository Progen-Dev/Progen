package de.mtorials.pwi.httpapi;

import de.mtorials.config.GuildConfiguration;
import de.mtorials.db.DAOHandler;
import de.progen_bot.core.Main;
import net.dv8tion.jda.api.entities.Member;

import java.util.Map;

public abstract class Endpiont {

    private String invoke;
    private DAOHandler daos = Main.getDAOs();

    public Endpiont(String invoke) {
        this.invoke = invoke;
    }

    public abstract APIResponseObject execute(Map<String, String> params, Member member, GuildConfiguration guildConfiguration);

    public String getInvoke() {
        return invoke;
    }
    public DAOHandler getDAOs() {
        return daos;
    }
}
