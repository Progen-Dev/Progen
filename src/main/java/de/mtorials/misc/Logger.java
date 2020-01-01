package de.mtorials.misc;

public class Logger {

    public static void log(String msg, int type) {

        if (type == 0) {
            System.out.println("[INFO] " + msg);
        } else if (type == 1) {
            System.out.println("[RequestExeption] " + msg);
        } else if (type == 2) {
            System.out.println("[WARNING] " + msg);
        }
    }

    public static void info(String msg) {
        log(msg, 0);
    }
}
