package de.mtorials.webinterface.httpapi;

import com.sun.net.httpserver.HttpServer;
import de.mtorials.webinterface.commands.APICommandMemberInfo;

import java.io.IOException;
import java.net.InetSocketAddress;

public class API {

    private HttpServer httpServer;
    private static APITokenManager tokenManager;

    API(int port) throws IOException {

        tokenManager = new APITokenManager();

        this.httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        httpServer.createContext("/api", new APICommandHandlerBuilder()
                .setTokenManager(tokenManager)
                .addCommand(new APICommandMemberInfo())
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
