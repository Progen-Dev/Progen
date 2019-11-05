package de.mtorials.pwi.endpoints;

import de.mtorials.config.GuildConfiguration;
import de.mtorials.pwi.httpapi.APIResponseObject;
import de.mtorials.pwi.httpapi.Endpiont;
import de.progen_bot.core.Main;
import net.dv8tion.jda.api.entities.Member;

import java.util.Map;

public class APIEPGetWarns extends Endpiont {

    public APIEPGetWarns() {
        super("getWarns");
    }

    @Override
    public APIResponseObject execute(Map<String, String> params, Member member, GuildConfiguration configuration) {
        return new APIResponseObject(200, Main.getDAOs().getWarnList().getWarnsByMember(member));
    }
}
