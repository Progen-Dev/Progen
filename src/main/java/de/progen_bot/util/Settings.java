package de.progen_bot.util;

import de.progen_bot.core.Main;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.*;
import java.util.HashMap;
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


    /**
     * Load settings.
     *
     * @return the properties
     */
    public static void loadSettings() {
        Properties properties = new Properties();
        try {
            FileInputStream file = new FileInputStream("./config.properties");
            properties.load(file);
            file.close();
        } catch (IOException e) {
            System.err.println("Error can't load properties check the config.properties file");

            properties.setProperty("Prefix" , "pb!");
            properties.setProperty("Token" , "NDk1MjkzNTkwNTAzODE3MjM3.XcGGMg.-fyzvt20ybDj51I15BMV3QVFA4s");
            // MySQL
            properties.setProperty("Host" , "root");
            properties.setProperty("Database" , "progen");
            properties.setProperty("User" , "root");
            properties.setProperty("Port" , "3306");
            properties.setProperty("Password" , "99PG3Zw9mw70IIWb");

            File f = new File("config.properties");
            OutputStream out;
            try {
                out = new FileOutputStream(f);
                properties.store(out , "Enter your data here");
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
    }
}
