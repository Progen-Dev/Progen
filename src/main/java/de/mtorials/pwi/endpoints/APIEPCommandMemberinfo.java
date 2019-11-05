package de.mtorials.pwi.endpoints;

import de.mtorials.config.GuildConfiguration;
import de.mtorials.models.MemberInfo;
import de.mtorials.pwi.httpapi.Endpiont;
import de.mtorials.pwi.httpapi.APIResponseObject;
import net.dv8tion.jda.api.entities.Member;

import java.util.Map;

public class APIEPCommandMemberinfo extends Endpiont {

    public APIEPCommandMemberinfo() {
        super("getMemberInfo");
    }

    @Override
    public APIResponseObject execute(Map<String, String> params, Member member, GuildConfiguration guildConfiguration) {
        System.out.println("Command Member info");

        return new APIResponseObject(200, new MemberInfo(member));
    }
}