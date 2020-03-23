package de.mtorials.pwi.httpapi;

import com.sun.net.httpserver.HttpServer;
import de.mtorials.pwi.endpoints.APIEPChangePrefix;
import de.mtorials.pwi.endpoints.APIEPMemberinfo;
import de.mtorials.pwi.endpoints.APIEPGetWarns;
import de.mtorials.pwi.endpoints.APIEPMusic;

import java.io.IOException;
import java.net.InetSocketAddress;

public class API {

    private HttpServer httpServer;
    private static APITokenManager tokenManager;

    public API(int port) {

        tokenManager = new APITokenManager();
        try {
            this.httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (httpServer == null)
            return;

        httpServer.createContext("/", new APICommandHandlerBuilder()
                .setTokenManager(tokenManager)
                .addCommand(new APIEPMemberinfo())
                .addCommand(new APIEPGetWarns())
                .addCommand(new APIEPChangePrefix())
                .addCommand(new APIEPMusic())
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
