package de.mtorials.pwi.endpoints;

import de.mtorials.entities.MemberInfo;
import de.mtorials.pwi.httpapi.APIResponseObject;
import de.mtorials.pwi.httpapi.Endpoint;
import de.progen_bot.db.entities.config.GuildConfiguration;
import net.dv8tion.jda.api.entities.Member;

import java.util.Map;

public class APIEPCommandMemberinfo extends Endpoint {

    public APIEPCommandMemberinfo() {
        super("getMemberInfo");
    }

    @Override
    public APIResponseObject execute(Map<String, String> params, Member member, GuildConfiguration guildConfiguration) {
        System.out.println("Command Member info");

        return new APIResponseObject(200, new MemberInfo(member));
    }
}
