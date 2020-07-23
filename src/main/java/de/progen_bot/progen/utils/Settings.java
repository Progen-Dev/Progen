package de.progen_bot.progen.utils;

import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Config of Progen
 *
 * @author Progen-Dev
 * @since 2.0.0
 */
public class Settings
{
    static {
        load();
    }

    // TODO Just for PermissionCore
    public static final List<String> BOT_ADMINS = new ArrayList<>();

    /**
     * Private constructor to hide no-args constructor of utility class
     */
    private Settings()
    {
        /* Prevent instantiation */
    }

    /**
     * Load Json file into config
     */
    public static void load()
    {
        /* TODO Load Json file */
    }

    /**
     * Get value of a given property from config file
     *
     * @param property property to get
     * @return {@link JsonElement JsonElement} of property
     */
    public static JsonElement get(String property)
    {
        load();

        /* TODO Get JsonElement from Json file */
        return null;
    }
}
