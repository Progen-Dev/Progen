package de.mtorials.webinterface.httpapi;


import com.sun.net.httpserver.HttpServer;
import de.mtorials.webinterface.commands.GetUserInfo;

import java.io.IOException;
import java.net.InetSocketAddress;

public class API {

    private HttpServer httpServer;

    API(int port) throws IOException {

        this.httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        httpServer.createContext("/api", new APICommandHandlerBuilder()
                .addCommand(new GetUserInfo())
                .build()
        );
        httpServer.setExecutor(null);
    }

    public void start() {

        httpServer.start();
    }

}
