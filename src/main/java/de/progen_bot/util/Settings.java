package de.progen_bot.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * The Class Settings.
 */
public class Settings {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static String json = null;

    static {
        loadSettings();
    }

    private static final JsonObject CONFIG = GSON.fromJson(json, JsonObject.class);

    private Settings() {
        /* Prevent instantiation */
    }

    //#region Bot
    public static final String PREFIX           =   get("bot", "prefix").getAsString();
    public static final String TOKEN            =   get("bot", "token").getAsString();
    public static final String API_PORT         =   String.valueOf(get("bot", "apiPort").getAsInt());

    public static final String MUSIC_TOKEN_1    =   get("bot", "musicToken1").getAsString();
    public static final String MUSIC_TOKEN_2    =   get("bot", "musicToken2").getAsString();

    public static final String TOP_GG_TOKEN     =   get("bot", "topGGToken").getAsString();
    //#endregion

    //#region Database
    public static final String HOST             =   get("database", "host").getAsString();
    public static final String DATABASE         =   get("database", "database").getAsString();
    public static final String PORT             =   String.valueOf(get("database", "port").getAsInt());
    public static final String USER             =   get("database", "user").getAsString();
    public static final String PASSWORD         =   get("database", "password").getAsString();
    //#endregion

    /**
     * Load config/settings of Progen.
     * If exists create new one, else load existing
     */
    public static void loadSettings() {
        try {
            json = new String(Files.readAllBytes(new File("config.json").toPath()));
        } catch (IOException e) {
            System.err.println("Failed to read config file");

            final JsonObject object = new JsonObject();

            final JsonObject bot = new JsonObject();
            bot.addProperty("prefix", "pb!");
            bot.addProperty("token", "");
            bot.addProperty("apiPort", 8083);
            bot.addProperty("topGGToken", "");
            bot.addProperty("musicToken1", "");
            bot.addProperty("musicToken2", "");

            object.add("bot", bot);

            final JsonObject database = new JsonObject();
            database.addProperty("host", "localhost");
            database.addProperty("database", "progen");
            database.addProperty("user", "progen");
            database.addProperty("password", "progen");
            database.addProperty("port", 3306);

            object.add("database", database);

            json = GSON.toJson(object);

            try {
                Files.write(new File("config.json").toPath(), json.getBytes(StandardCharsets.UTF_8));
            } catch (IOException ex) {
                System.err.println("Failed to write to config file");
                ex.printStackTrace();
            }
        }
    }

    public static JsonElement get(String jsonObjectName, String property) {
        loadSettings();

        final JsonElement propertyObject;

        try {
            propertyObject = CONFIG.getAsJsonObject(jsonObjectName).get(property);
        } catch(JSONException e) {
            throw new IllegalArgumentException(jsonObjectName + "." + property + "does not exist in config");
        }

        return propertyObject;
    }
}
