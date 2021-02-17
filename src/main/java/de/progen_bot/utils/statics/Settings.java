package de.progen_bot.utils.statics;

import com.google.gson.*;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public class Settings
{
    private static final String BOT_KEY = "bot";
    private static final String DATABASE_KEY = "database";
    private static final String PROGEN = "progen";

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static JsonObject config;

    //#region Bot
    public static final List<String> BOT_OWNERS =   List.of(GSON.fromJson(get(BOT_KEY, "owners").getAsJsonArray(), String[].class));
    public static final String PREFIX           =   get(BOT_KEY, "prefix").getAsString();
    public static final String TOKEN            =   get(BOT_KEY, "token").getAsString();
    public static final String API_PORT         =   String.valueOf(get(BOT_KEY, "apiPort").getAsInt());

    public static final List<String> MUSIC      =   List.of(GSON.fromJson(get(BOT_KEY, "music").getAsJsonArray(), String[].class));

    public static final String TOP_GG_TOKEN     =   get(BOT_KEY, "topGGToken").getAsString();

    //#region Database
    public static final String HOST             =   get(DATABASE_KEY, "host").getAsString();
    public static final String DATABASE         =   get(DATABASE_KEY, DATABASE_KEY).getAsString();
    public static final String PORT             =   String.valueOf(get(DATABASE_KEY, "port").getAsInt());
    public static final String USER             =   get(DATABASE_KEY, "user").getAsString();
    public static final String PASSWORD         =   get(DATABASE_KEY, "password").getAsString();
    //#endregion
    //#endregion

    static
    {
        loadSettings();
    }

    // TODO: 17.02.2021 maybe class parsing
    public static void loadSettings()
    {
        String json = null;
        try
        {
            json = new String(Files.readAllBytes(new File("config.json").toPath()));
            config = GSON.fromJson(json, JsonObject.class);
        }
        catch (IOException e)
        {
            System.err.println("Failed to read config file");

            final JsonObject object = new JsonObject();

            final JsonObject bot = new JsonObject();
            bot.addProperty("prefix", "pb!");
            bot.addProperty("token", "");
            bot.addProperty("apiPort", 8083);
            bot.addProperty("topGGToken", "");

            final JsonArray musicTokens = new JsonArray();
            bot.add("music", musicTokens);

            final JsonArray owners = new JsonArray();
            owners.add("402140322525872138");
            owners.add("279271145205923847");
            owners.add("225327305570910208");
            owners.add("321227144791326730");
            bot.add("owners", owners);

            object.add(BOT_KEY, bot);

            final JsonObject database = new JsonObject();
            database.addProperty("host", "localhost");
            database.addProperty(DATABASE_KEY, PROGEN);
            database.addProperty("user", PROGEN);
            database.addProperty("password", PROGEN);
            database.addProperty("port", 3306);

            object.add(DATABASE_KEY, database);

            json = GSON.toJson(object);

            try
            {
                Files.write(new File("config.json").toPath(), json.getBytes(StandardCharsets.UTF_8));
            }
            catch (IOException ex)
            {
                System.err.println("Failed to write to config file");
                ex.printStackTrace();
            }
        }
    }

    public static JsonElement get(String jsonObjectName, String property)
    {
        loadSettings();

        final JsonElement propertyObject;

        try
        {
            propertyObject = config.getAsJsonObject(jsonObjectName).get(property);
        } catch (JSONException e)
        {
            throw new IllegalArgumentException(jsonObjectName + "." + property + " does not exist in config");
        }

        return propertyObject;
    }

    public static Gson getGson()
    {
        return GSON;
    }

    public static JsonObject getConfig()
    {
        return config;
    }
}
