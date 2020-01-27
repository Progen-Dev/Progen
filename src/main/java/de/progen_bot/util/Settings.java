package de.progen_bot.util;

import java.io.*;
import java.util.Properties;

/**
 * The Class Settings.
 */
public class Settings {
    public static String PREFIX;
    public static String TOKEN;

    public static String HOST;
    public static String DATABASE;
    public static String PORT;
    public static String USER;
    public static String PASSWORD;

    public static String MUSICTOKEN1;
    public static String MUSICTOKEN2;

    public static String TOPGGTOKEN;


    /**
     * Load settings.
     *
     */
    public static void loadSettings() {
        Properties properties = new Properties();
        try {
            FileInputStream file = new FileInputStream("./config.properties");
            properties.load(file);
            file.close();
        } catch (IOException e) {
            System.err.println("Error can't load properties check the config.properties file");

            properties.setProperty("Prefix", "");
            // MySQL
            properties.setProperty("Host", "");
            properties.setProperty("Database", "");
            properties.setProperty("User", "");
            properties.setProperty("Port", "");
            properties.setProperty("Password", "");

            properties.setProperty("musictoken1", "");
            properties.setProperty("musictoken2", "");

            properties.setProperty("topggtoken", "");

            File f = new File("config.properties");
            OutputStream out;
            try {
                out = new FileOutputStream(f);
                properties.store(out, "Enter your data here");
                out.close();
                System.out.println("Please enter your data in the config file!");
                System.exit(0);
            } catch (IOException e1) {
                System.err.println("Error can't create config file");
                e1.printStackTrace();
            }
            e.printStackTrace();
        }

        PREFIX = properties.getProperty("Prefix");
        TOKEN = properties.getProperty("Token");

        HOST = properties.getProperty("Host");
        PORT = properties.getProperty("Port");
        USER = properties.getProperty("User");
        PASSWORD = properties.getProperty("Password");
        DATABASE = properties.getProperty("Database");

        MUSICTOKEN1 = properties.getProperty("musictoken1");
        MUSICTOKEN2 = properties.getProperty("musictoken2");

        TOPGGTOKEN = properties.getProperty("topggtoken");
    }
}
