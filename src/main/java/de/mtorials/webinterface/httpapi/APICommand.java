package de.mtorials.webinterface.httpapi;

import java.util.HashMap;

public abstract class APICommand {

    private String invoke;

    public APICommand(String invoke) {
        this.invoke = invoke;
    }

    public abstract void execute(HashMap<String, String> params);

    public String getInvoke() {
        return invoke;
    }
}
