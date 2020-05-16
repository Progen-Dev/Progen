package de.progen_bot.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

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

    public static final String PREFIX = CONFIG.getAsJsonObject("bot").get("prefix").getAsString();
    public static final String TOKEN = CONFIG.getAsJsonObject("bot").get("token").getAsString();

    public static final String HOST = CONFIG.getAsJsonObject("database").get("host").getAsString();
    public static final String DATABASE = CONFIG.getAsJsonObject("database").get("database").getAsString();
    public static final String PORT = String.valueOf(CONFIG.getAsJsonObject("database").get("port").getAsInt());
    public static final String USER = CONFIG.getAsJsonObject("database").get("user").getAsString();
    public static final String PASSWORD = CONFIG.getAsJsonObject("database").get("password").getAsString();

    public static final String API_PORT = String.valueOf(CONFIG.getAsJsonObject("bot").get("apiPort").getAsInt());

    public static final String MUSIC_TOKEN_1 = CONFIG.getAsJsonObject("bot").get("musicToken1").getAsString();
    public static final String MUSIC_TOKEN_2 = CONFIG.getAsJsonObject("bot").get("musicToken2").getAsString();

    public static final String TOP_GG_TOKEN = CONFIG.getAsJsonObject("bot").get("topGGToken").getAsString();

    /**
     * Load settings.
     *
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
}
