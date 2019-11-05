package de.mtorials.pwi.httpapi;

import java.util.ArrayList;

public class APICommandHandlerBuilder {

    private ArrayList<Endpiont> commands = new ArrayList<>();
    private APITokenManager tokenManager;

    public APICommandHandlerBuilder addCommand(Endpiont command) {

        this.commands.add(command);
        return this;
    }

    public APICommandHandlerBuilder setTokenManager(APITokenManager tokenManager) {

        this.tokenManager = tokenManager;
        return this;
    }

    public APICommandHandler build() {

        return new APICommandHandler(this.commands, this.tokenManager);
    }
}
