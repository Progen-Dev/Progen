package de.mtorials.webinterface.commands;

import de.mtorials.webinterface.httpapi.APICommand;
import de.mtorials.webinterface.httpapi.APICommandHandler;

import java.util.HashMap;

public class GetUserInfo extends APICommand {


    public GetUserInfo() {
        super("getUserInfo");
    }

    @Override
    public void execute(HashMap<String, String> params) {
    }
}
