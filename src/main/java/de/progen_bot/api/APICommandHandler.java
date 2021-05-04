package de.progen_bot.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import de.progen_bot.api.endpoints.Endpoint;
import de.progen_bot.api.entities.APIResponseObject;
import de.progen_bot.api.exceptions.APICommandNotFoundException;
import de.progen_bot.api.exceptions.APIException;
import de.progen_bot.database.dao.config.ConfigDaoImpl;
import de.progen_bot.database.entities.config.GuildConfiguration;
import de.progen_bot.utils.logger.Logger;
import net.dv8tion.jda.api.entities.Member;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class APICommandHandler implements HttpHandler
{
    private final List<Endpoint> commands;
    private final APITokenManager tokenManager;

    private APICommandHandler(List<Endpoint> commands, APITokenManager tokenManager)
    {
        this.commands = commands;
        this.tokenManager = tokenManager;
    }

    private static Map<String, String> parseQueryString(String qs)
    {
        final Map<String, String> result = new HashMap<>();
        int last = 0, next, l = qs.length();
        while (last < l)
        {
            next = qs.indexOf('&', last);
            if (next == -1)
                next = l;

            if (next > last)
            {
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

    private static String toJSON(Object o)
    {
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            return mapper.writeValueAsString(o);
        }
        catch (JsonProcessingException e)
        {
            System.out.println("JSON Parser exception");
            e.printStackTrace();
            return "ERRORJSON";
        }
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        final String uri = exchange.getRequestURI().toString();
        final Map<String, String> params = parseQueryString(uri.split("\\?", 2)[1]);
        final String currentInvoke = uri.split("/")[1].split("\\?")[0];
        String response;
        int rCode;

        Logger.info("REQUEST " + currentInvoke);

        try
        {
            final APIResponseObject responseObject = handleCommands(currentInvoke, params);
            response = toJSON(responseObject);
            rCode = responseObject.getrCode();

        }
        catch (APIException e)
        {
            rCode = 500;
            response = "ERROR";
        }

        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(rCode, response.getBytes().length);

        final OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.flush();
        os.close();
    }

    private APIResponseObject handleCommands(String currentInvoke, Map<String, String> params)
    {
        boolean commandNotFound = true;
        APIResponseObject returnObject = null;

        for (Endpoint command : this.commands)
        {
            if (currentInvoke.equals(command.getInvoke()))
            {
                final Member member = tokenManager.getMember(params.get("token"));
                final GuildConfiguration config = new ConfigDaoImpl().loadConfig(member.getGuild().getIdLong());
                returnObject = command.execute(params, member, config);
                commandNotFound = false;
            }
        }

        if (commandNotFound)
            throw new APICommandNotFoundException();

        return returnObject;
    }

    public static class Builder
    {
        private final List<Endpoint> commands = new ArrayList<>();
        private APITokenManager tokenManager;

        public Builder addCommand(Endpoint endpoint)
        {
            if (this.commands.contains(endpoint) || this.commands.stream().map(Endpoint::getInvoke).anyMatch(invoke -> invoke.equalsIgnoreCase(endpoint.getInvoke())))
                throw new IllegalArgumentException("Endpoint already registered");

            this.commands.add(endpoint);

            return this;
        }

        public APICommandHandler build()
        {
            return new APICommandHandler(this.commands, this.tokenManager);
        }

        public List<Endpoint> getCommands()
        {
            return commands;
        }

        public APITokenManager getTokenManager()
        {
            return tokenManager;
        }

        public Builder setTokenManager(APITokenManager tokenManager)
        {
            this.tokenManager = tokenManager;

            return this;
        }
    }
}
