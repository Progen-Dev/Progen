package de.mtorials.webinterface.httpapi;

import net.dv8tion.jda.api.entities.Member;

import java.util.Map;

public abstract class APICommand {

    private String invoke;

    public APICommand(String invoke) {
        this.invoke = invoke;
    }

    public abstract APIResponseObject execute(Map<String, String> params, Member member);

    public String getInvoke() {
        return invoke;
    }
}
