package de.mtorials.webinterface.httpapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import de.mtorials.webinterface.exceptions.APICommandNotFoundException;
import de.mtorials.webinterface.exceptions.APIException;
import net.dv8tion.jda.api.entities.Member;

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

    public void handle(HttpExchange exchange) throws IOException {

        System.out.println("Get Request");

        String uri = exchange.getRequestURI().toString();
        Map<String, String> params = parseQueryString(uri.split("\\?", 2)[1]);
        String currentInvoke = uri.split("/")[1].split("\\?")[0];

        String response = "";
        int rCode;

        try {

            APIResponseObject responseObject = handleCommands(currentInvoke, params);
            response = toJSON(responseObject);
            rCode = responseObject.getrCode();

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

    private APIResponseObject handleCommands(String currentInvoke, Map<String, String> params) {

        boolean commandNotFound = true;
        APIResponseObject returnObject = null;
        for (APICommand command : registeredCommands) {

            if (currentInvoke.equals(command.getInvoke())) {

                Member member = tokenManager.getMember(params.get("token"));
                returnObject = command.execute(params, member);
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

    private static String toJSON(Object o) {

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            System.out.println("JSON Parser exeption");
            e.printStackTrace();
            return "ERRORJSON";
        }
    }
}
