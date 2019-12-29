package de.mtorials.pwi.httpapi;

import com.sun.net.httpserver.HttpServer;
import de.mtorials.pwi.endpoints.APIEPChangePrefix;
import de.mtorials.pwi.endpoints.APIEPCommandMemberinfo;

import java.io.IOException;
import java.net.InetSocketAddress;

public class API {

    private HttpServer httpServer;
    private static APITokenManager tokenManager;

    public API(int port) throws IOException {

        tokenManager = new APITokenManager();

        this.httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        httpServer.createContext("/", new APICommandHandlerBuilder()
                .setTokenManager(tokenManager)
                .addCommand(new APIEPCommandMemberinfo())
                .addCommand(new APIEPChangePrefix())
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
