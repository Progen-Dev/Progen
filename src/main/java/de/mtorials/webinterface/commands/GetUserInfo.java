package de.mtorials.webinterface.commands;

import de.mtorials.models.MemberInfo;
import de.mtorials.webinterface.httpapi.APICommand;
import net.dv8tion.jda.core.entities.Member;

import java.util.Map;

public class GetUserInfo extends APICommand {

    public GetUserInfo() {
        super("getUserInfo");
    }

    @Override
    public void execute(Map<String, String> params, Member member) {

        MemberInfo info = new MemberInfo(member);

    }
}
