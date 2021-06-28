package de.progen_bot.api;

import com.sun.net.httpserver.HttpServer;
import de.progen_bot.api.endpoints.APIEPChangePrefix;
import de.progen_bot.api.endpoints.APIEPGetWarns;
import de.progen_bot.api.endpoints.APIEPMemberInfo;
import de.progen_bot.api.endpoints.APIEPMusic;

import java.io.IOException;
import java.net.InetSocketAddress;

public class API
{
    private static APITokenManager tokenManager;
    private final HttpServer server;

    public API(int port )
    {
        tokenManager = new APITokenManager();

        try
        {
            this.server = HttpServer.create(new InetSocketAddress(port), 0);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }

        this.server.createContext("/", new APICommandHandler.Builder()
                .setTokenManager(tokenManager)
                .addCommand(new APIEPChangePrefix())
                .addCommand(new APIEPGetWarns())
                .addCommand(new APIEPMemberInfo())
                .addCommand(new APIEPMusic())
                .build()
        );

        this.server.setExecutor(null);
    }

    public void start()
    {
        this.server.start();
    }

    public HttpServer getServer()
    {
        return server;
    }

    public static APITokenManager getTokenManager()
    {
        return tokenManager;
    }
}
