package de.mtorials.webinterface.httpapi;


import com.sun.net.httpserver.HttpServer;
import de.mtorials.webinterface.commands.GetUserInfo;
import de.progen_bot.core.Main;

import java.io.IOException;
import java.net.InetSocketAddress;

public class API {

    private HttpServer httpServer;
    private static APITokenManager tokenManager;

    API(int port) throws IOException {

        tokenManager = new APITokenManager(Main.getJda());

        this.httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        httpServer.createContext("/api", new APICommandHandlerBuilder()
                .setTokenManager(tokenManager)
                .addCommand(new GetUserInfo())
                .build()
        );
        httpServer.setExecutor(null);
    }

    public void start() {

        httpServer.start();
    }

    public static APITokenManager getTokenManager() {
        return tokenManager;
    }
}
