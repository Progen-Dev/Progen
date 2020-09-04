package de.mtorials.pwi.endpoints;

import de.mtorials.pwi.httpapi.APIResponseObject;
import de.mtorials.pwi.httpapi.Endpoint;
import de.progen_bot.db.dao.config.ConfigDaoImpl;
import de.progen_bot.db.entities.config.GuildConfiguration;
import net.dv8tion.jda.api.entities.Member;

import java.util.Map;

public class APIEPChangePrefix extends Endpoint {

    public APIEPChangePrefix() {
        super("changePrefix");
    }

    @Override
    public APIResponseObject execute(Map<String, String> params, Member member, GuildConfiguration configuration) {

        if (!params.containsKey("newPrefix")) return new APIResponseObject(400, null);
        if (!member.isOwner()) return new APIResponseObject(401, null);

        configuration.setPrefix(params.get("newPrefix"));
        new ConfigDaoImpl().writeConfig(configuration, member.getGuild());

        return new APIResponseObject(200, null);
    }
}
