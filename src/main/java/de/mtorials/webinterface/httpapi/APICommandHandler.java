package de.mtorials.webinterface.httpapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import de.mtorials.webinterface.exceptions.APICommandNotFoundException;
import de.mtorials.webinterface.exceptions.APIException;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class APICommandHandler implements HttpHandler {

    private ArrayList<APICommand> registeredCommands;
    private APITokenManager tokenManager;

    public APICommandHandler(ArrayList<APICommand> commands, APITokenManager tokenManager) {

        this.registeredCommands = commands;
        this.tokenManager = tokenManager;
    }

    public void handle(HttpExchange exchange) throws IOException{

        Map<String, String> params = parseQueryString(exchange.getRequestURI().toString().split("\\?", 2)[2]);
        String currentInvoke = exchange.getRequestURI().toString().split("/")[1];

        String response;
        int rCode = 200;

        try {

            response = new ObjectMapper().writeValueAsString(handleCommands(currentInvoke, params));

        } catch (APIException e) {

            rCode = 500;
            response = "ERROR";
        }

        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(rCode, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.flush();
        os.close();

    }

    private Object handleCommands(String currentInvoke, Map<String, String> params) {

        boolean commandNotFound = true;
        Object returnObject = new Object();
        for (APICommand command : registeredCommands) {

            if (currentInvoke.equals(command.getInvoke())) {

                returnObject = command.execute(params, tokenManager.getMember(params.get("token")));
                commandNotFound = false;
            }
        }
        if (commandNotFound) throw new APICommandNotFoundException();
        return returnObject;
    }

    private static Map<String, String> parseQueryString(String qs) {
        Map<String, String> result = new HashMap<>();

        int last = 0, next, l = qs.length();
        while (last < l) {
            next = qs.indexOf('&', last);
            if (next == -1)
                next = l;

            if (next > last) {
                int eqPos = qs.indexOf('=', last);
                if (eqPos < 0 || eqPos > next)
                    result.put(URLDecoder.decode(qs.substring(last, next), StandardCharsets.UTF_8), "");
                else
                    result.put(URLDecoder.decode(qs.substring(last, eqPos), StandardCharsets.UTF_8), URLDecoder.decode(qs.substring(eqPos + 1, next), StandardCharsets.UTF_8));
            }
            last = next + 1;
        }
        return result;
    }
}
