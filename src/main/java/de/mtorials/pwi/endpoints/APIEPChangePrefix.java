package de.mtorials.pwi.endpoints;

import de.mtorials.config.GuildConfiguration;
import de.mtorials.config.GuildConfigurationBuilder;
import de.mtorials.pwi.httpapi.Endpiont;
import de.mtorials.pwi.httpapi.APIResponseObject;
import de.progen_bot.core.Main;
import net.dv8tion.jda.api.entities.Member;

import java.util.Map;

public class APIEPChangePrefix extends Endpiont {

    public APIEPChangePrefix() {
        super("changePrefix");
    }

    @Override
    public APIResponseObject execute(Map<String, String> params, Member member, GuildConfiguration configuration) {

        if (!params.containsKey("newPrefix")) return new APIResponseObject(400, null);
        if (!member.isOwner()) return new APIResponseObject(401, null);

        configuration.prefix = params.get("newPrefix");
        super.getDAOs().getConfig().writeConfig(configuration, member.getGuild());

        return new APIResponseObject(200, null);
    }
}
