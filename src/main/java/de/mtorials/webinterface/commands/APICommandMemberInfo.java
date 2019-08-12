package de.mtorials.webinterface.commands;

import de.mtorials.models.MemberInfo;
import de.mtorials.webinterface.httpapi.APICommand;
import de.mtorials.webinterface.httpapi.APIResponseObject;
import net.dv8tion.jda.api.entities.Member;

import java.util.Map;

public class APICommandMemberInfo extends APICommand {

    public APICommandMemberInfo() {
        super("getMemberInfo");
    }

    @Override
    public APIResponseObject execute(Map<String, String> params, Member member) {

        System.out.println("Command Member info");

        return new APIResponseObject(200, new MemberInfo(member));
    }
}
