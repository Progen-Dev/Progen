package de.mtorials.webinterface.httpapi;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class APICommandHandlerBuilder {

    private ArrayList<APICommand> commands = new ArrayList<>();
    private APITokenManager tokenManager;

    public APICommandHandlerBuilder addCommand(APICommand command) {

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
