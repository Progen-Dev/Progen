package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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

			properties.setProperty("Prefix", "pb!");
			properties.setProperty("Token", "");
			// MySQL
			properties.setProperty("Host", "");
			properties.setProperty("Database", "");
			properties.setProperty("User", "");
			properties.setProperty("Port", "");
			properties.setProperty("Password", "");

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
	}
}
