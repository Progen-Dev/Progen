package de.pwi.api.endpoints;

import de.pwi.api.httpapi.APIResponseObject;
import de.pwi.api.httpapi.Endpoint;
import de.progen_bot.db.dao.warnlist.WarnListDaoImpl;
import de.progen_bot.db.entities.config.GuildConfiguration;
import net.dv8tion.jda.api.entities.Member;

import java.util.List;
import java.util.Map;

public class APIEPGetWarns extends Endpoint {

    public APIEPGetWarns() {
        super("getWarns");
    }

    @Override
    public APIResponseObject execute(Map<String, String> params, Member member, GuildConfiguration configuration) {
        List<String> warns = new WarnListDaoImpl().loadWarnList(member);
        return new APIResponseObject(200, warns);

    }
}
