package de.pwi.api.httpapi;

import com.sun.net.httpserver.HttpServer;
import de.pwi.api.endpoints.APIEPChangePrefix;
import de.pwi.api.endpoints.APIEPMemberInfo;
import de.pwi.api.endpoints.APIEPGetWarns;
import de.pwi.api.endpoints.APIEPMusic;

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
                .addCommand(new APIEPMemberInfo())
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
