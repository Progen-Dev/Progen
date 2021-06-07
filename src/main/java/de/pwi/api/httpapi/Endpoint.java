package de.pwi.api.httpapi;

import de.progen_bot.core.Main;
import de.progen_bot.db.DaoHandler;
import de.progen_bot.db.entities.config.GuildConfiguration;
import net.dv8tion.jda.api.entities.Member;

import java.util.Map;

public abstract class Endpoint {

    private final String invoke;
    private final DaoHandler daos = Main.getDAOs();

    public Endpoint(String invoke) {
        this.invoke = invoke;
    }

    public abstract APIResponseObject execute(Map<String, String> params, Member member, GuildConfiguration guildConfiguration);

    public String getInvoke() {
        return invoke;
    }

    public DaoHandler getDAOs() {
        return daos;
    }
}
