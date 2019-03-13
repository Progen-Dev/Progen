package de.mtorials.webinterface.commands;

import de.mtorials.models.MemberInfo;
import de.mtorials.webinterface.httpapi.APICommand;
import net.dv8tion.jda.core.entities.Member;

import java.util.Map;

public class APICommandMemberInfo extends APICommand {

    public APICommandMemberInfo() {
        super("getUserInfo");
    }

    @Override
    public Object execute(Map<String, String> params, Member member) {

        return new MemberInfo(member);
    }
}
