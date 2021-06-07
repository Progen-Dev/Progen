package de.pwi.api.endpoints;

import de.pwi.entities.json.MemberInfo;
import de.pwi.api.httpapi.APIResponseObject;
import de.pwi.api.httpapi.Endpoint;
import de.progen_bot.db.entities.config.GuildConfiguration;
import net.dv8tion.jda.api.entities.Member;

import java.util.Map;

public class APIEPMemberInfo extends Endpoint {

    public APIEPMemberInfo() {
        super("getMemberInfo");
    }

    @Override
    public APIResponseObject execute(Map<String, String> params, Member member, GuildConfiguration guildConfiguration) {
        System.out.println("Command Member info");

        return new APIResponseObject(200, new MemberInfo(member));
    }
}
